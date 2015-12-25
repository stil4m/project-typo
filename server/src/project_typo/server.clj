(ns project-typo.server
  (:require [aleph.http :as http]
            [byte-streams :as bs]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [manifold.bus :as bus]
            [manifold.deferred :as d]
            [manifold.stream :as s]
            [manifold.stream :as stream]
            [project-typo.channel :as channel]
            [project-typo.messages :as messages]
            [project-typo.users :as users]
            [project-typo.transit :refer [decode-message encode-message]]
            [clj-time.coerce :as coerce])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn update-channel-access
  [channel-access channels]
  (reduce
   (fn [access channel]
     (when (not (get @access (:id channel)))
       (swap! access assoc (:id channel) (:members channel)))
     access)
   channel-access
   channels))

(defn publish-to
  ([event-bus members event data]
   (publish-to event-bus members event data nil))
  ([event-bus members event data message-id]
   ;TODO At this point it would be great to have a plugin point
   (let [message (encode-message {:event event :data data :message-id message-id})]
     (doall (map
             #(bus/publish! event-bus % message)
             members)))))

(defmulti action (fn [conn component m] (:action m)))

(defmethod action :create-channel create-channel [{:keys [username conn]}
                                   {:keys [event-bus channel-access channel-service]}
                                   msg]
  (let [res (channel/create channel-service (:data msg) username)]
    (log/info "created channel" res)
    (update-channel-access channel-access [res])
    (publish-to event-bus (:members res) :channel-created res (:message-id msg))))

(defmethod action :join-channel join-channel [{:keys [subscriptions conn]}
                                 {:keys [event-bus channel-access message-service]}
                                 {:keys [data message-id]}]
  (let [channel (:channel data)
        most-recent (messages/most-recent message-service channel 100)]
    (s/put! conn (encode-message {:message-id message-id
                                  :event :joined-channel
                                  :data {:most-recent-messages most-recent :channel channel}}))))

(defmethod action :leave-channel leave-channel [{:keys [conn]}
                                  {:keys [event-bus channel-service]}
                                  {:keys [channel-id]}]
  (bus/downstream event-bus channel-id))

(defmethod action :list-people list-people [{:keys [conn]}
                                            {:keys [user-service]}
                                            _]
  (s/put! conn (encode-message {:event :all-people
                                :data {:people (users/list-all user-service)}})))

(defmethod action :list-channels list-channels [context component _]
  (let [{:keys [channel-service channel-access]} component
        {:keys [conn username]} context
        channels (channel/list-all channel-service username)]

    ;Adds missing channels to channel-access
    (update-channel-access channel-access channels)
    (s/put! conn (encode-message {:event :all-channels
                                  :data {:channels channels}}))))

(defmethod action :send-message message [{:keys [conn username]}
                            {:keys [event-bus channel-access message-service]}
                            {:keys [data] :as msg}]
  (let [channel (:channel data)
        created-message (messages/create
                         message-service
                         (->
                          (select-keys data [:channel :body :client-id])
                          (assoc :user username)))]
    (publish-to event-bus (get @channel-access channel) :new-message created-message)))

(defmethod action :default [_ _ msg]
  (log/warn "Unhandled action" msg))

(def non-websocket-request
  {:status 400
   :headers {"content-type" "application/text"}
   :body "Expected a websocket request."})

(defn start-connection [component conn]
  (d/let-flow [msg (s/take! conn)
               authentication (decode-message msg)
               authentication-call? (= :authenticate (:action authentication))
               connections (:connections component)
               event-bus (:event-bus component)
               username (get-in authentication [:data :username])
               context {:username username
                        :full-name (get-in authentication [:data :full-name])
                        :conn conn
                        :subscriptions (atom {})}]

              (s/put! conn
                      (encode-message
                       {:event :authentication
                        :data {:result (if authentication-call? :ok :failed)}}))

              (users/update-user (:user-service component) (:data authentication))

              (if (not authentication-call?)
                (s/close! conn))

              (when authentication-call?
                ;; Add to local state
                (swap! connections assoc conn context)

                ;; Register to on closed callback
                (s/on-closed conn
                             (fn [] (swap! connections dissoc conn)))

                ;; Start Heartbeat
                (s/connect
                 (s/periodically 10000 #(encode-message {:event :heartbeat}))
                 conn #_(s/buffer 100 conn)
                 {:timeout 1e4})

                ;; Subscribe for messages directed to all
                (s/connect
                 (bus/subscribe event-bus :all)
                 conn
                 {:timeout 1e4})

                ;; Subscribe for messages directed to the user
                (log/info "Subscribe for:" (get-in authentication [:data :username]))
                (s/connect
                 (bus/subscribe event-bus (get-in authentication [:data :username]))
                 conn
                 {:timeout 1e4})

                ;; Consume messsages from connected client
                (s/consume
                 #(action context component (decode-message %))
                 (->> conn
                      (s/buffer 100)
                      ;; Only one message per second or 10 seconds worth if they were gone for 10 seconds
                      (s/throttle 5 15))))))

(defn chat-handler
  [component req]
  (d/let-flow [conn (d/catch
                        (http/websocket-connection req)
                        (fn [_] nil))]
              (if-not conn
                non-websocket-request
                (start-connection component conn))))

(defrecord Server [port instance channel-service user-service message-service event-bus]
  component/Lifecycle
  (start [component]
    (log/info "Starting server on port " port)
    (let [connections (atom {})
          channel-access (atom {})
          instance (http/start-server (partial chat-handler (assoc component
                                                             :connections connections
                                                             :channel-access channel-access)) {:port port})]
      (assoc component
             :instance instance
             :connections connections)))
  (stop [component]
    (log/info "Stopping server..")
    (when-let [instance (:instance component)]
      (log/info "Closing server..")
      (.close instance)
      component)))

(defn create-server [port]
  (map->Server {:port port}))

