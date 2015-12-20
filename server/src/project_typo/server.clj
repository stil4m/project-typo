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
            [project-typo.transit :refer [decode-message encode-message]]
            [rethinkdb.query :as r]
            [clj-time.coerce :as coerce])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))


(defmulti action (fn [conn component m] (:action m)))

(defmethod action :create-channel create-channel [{:keys [conn]}
                                   {:keys [event-bus channel-service]}
                                   {:keys [name]}]
  (let [res (channel/create channel-service {:name name})]
    (log/info "created channel" res)
    (s/put! conn (encode-message {:event :channel-created
                                  :created-channel res}))))

(defmethod action :join-channel join-channel [{:keys [subscriptions conn]}
                                 {:keys [event-bus message-service]}
                                 {:keys [channel]}]
  (s/connect
   (bus/subscribe event-bus channel)
   (s/buffer 100 conn)
   {:timeout 1e4})
  (bus/publish! event-bus channel (encode-message
                                   {:most-recent-messages (messages/most-recent message-service channel 100)
                                    :channel channel
                                    :event :joined-channel})))

(defmethod action :leave-channel leave-channel [{:keys [conn]}
                                  {:keys [event-bus channel-service]}
                                  {:keys [channel-id]}]
  (bus/downstream event-bus channel-id))

(defmethod action :list-channels list-channels [{:keys [conn]}
                                  {:keys [channel-service]}
                                  _]
  (s/put! conn (encode-message {:event :all-channels
                                :channels (channel/list-all channel-service)})))

(defmethod action :message message [{:keys [conn username]}
                            {:keys [event-bus message-service]}
                            {:keys [channel body] :as msg}]
  (log/info "got message" msg)
  (let [created-message (messages/create
                         message-service
                         (->
                          (select-keys msg [:channel :body :client-id])
                          (assoc :user username)))]
    (log/info "created message" msg)
    (bus/publish!
     event-bus
     channel
     (encode-message
      (assoc
       created-message
       :event
       :new-message)))))

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
               context {:username (get-in authentication [:identity :username])
                        :full-name (get-in authentication [:identity :full-name])
                        :conn conn
                        :subscriptions (atom {})}]

              (s/put! conn
                      (encode-message
                       {:event :authentication
                        :result (if authentication-call? :ok :failed)}))

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
                 (s/buffer 100 conn)
                 {:timeout 1e4})

                ;; Consume messsages from connected client
                (s/consume
                 #(action context component (decode-message %))
                 (->> conn
                      (s/buffer 100)
                      ;; Only one message per second or 10 seconds worth if they were gone for 10 seconds
                      (s/throttle 1 10))))))

(defn chat-handler
  [component req]
  (d/let-flow [conn (d/catch
                        (http/websocket-connection req)
                        (fn [_] nil))]
              (if-not conn
                non-websocket-request
                (start-connection component conn))))

(defrecord Server [port instance channel-service message-service event-bus]
  component/Lifecycle
  (start [component]
    (log/info "Starting server on port " port)
    (let [connections (atom {})
          instance (http/start-server (partial chat-handler (assoc component
                                                             :connections connections)) {:port port})]
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
