(ns project-typo.server
  (:require [aleph.http :as http]
            [byte-streams :as bs]
            [clojure.tools.logging :as log]
            [cognitect.transit :as transit]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [manifold.bus :as bus]
            [manifold.deferred :as d]
            [manifold.stream :as s]
            [manifold.stream :as stream]
            [project-typo.channel :as channel]
            [rethinkdb.query :as r])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn decode-message [msg]
  (let [in (ByteArrayInputStream. (.getBytes msg))
        reader (transit/reader in :json)]
    (transit/read reader)))

(defn encode-message [msg]
  (let [out (ByteArrayOutputStream. 4096)
        writer (transit/writer out :json)]
    (transit/write writer msg)
    (.toString out)))

(defmulti action (fn [conn component m] (:action m)))

(defmethod action :create-channel [conn {:keys [event-bus channel-service]} m]
  (let [res (channel/create channel-service {:name (:channel m)})]
    (log/info "Created channel" res)
    (s/put! conn (encode-message {:event :channel-created
                                  :created-channel res}))))

(defmethod action :join-channel [conn {:keys [event-bus channel-service]} {:keys [channel-id]}]
  (s/connect
   (bus/subscribe event-bus channel-id)
   (->> conn
        (s/buffer 100))
   {:upstream? true
    :downstream? true}))

(defmethod action :leave-channel [conn {:keys [event-bus channel-service]} {:keys [channel-id]}]
  (bus/downstream event-bus channel-id))

(defmethod action :message [_ {:keys [event-bus]} {:keys [channel body] :as msg}]
  (log/info "got message" msg)
  (bus/publish! event-bus channel (encode-message msg)))

(defmethod action :default [_ _ msg]
  (log/warn "Unhandled action" msg))

(def non-websocket-request
  {:status 400
   :headers {"content-type" "application/text"}
   :body "Expected a websocket request."})

(defn chat-handler
  [component req]
  (d/let-flow [conn (d/catch
                        (http/websocket-connection req)
                        (fn [_] nil))]
              (if-not conn
                ;; if it wasn't a valid websocket handshake, return an error
                non-websocket-request
                (d/let-flow [channel (s/take! conn)]
                            (action conn component (decode-message channel))
                            (s/consume
                             #(action conn component (decode-message %))
                             (->> conn
                                  (s/buffer 100)))))))

(defrecord Server [port instance channel-service event-bus]
  component/Lifecycle
  (start [component]
    (log/info "Starting server on port " port)
    (let [instance (http/start-server (partial chat-handler component) {:port port})]
      (assoc component :instance instance)))
  (stop [component]
    (log/info "Stopping server..")
    (when-let [instance (:instance component)]
      (log/info "Closing server..")
      (.close instance)
      component)))

(defn create-server [port]
  (map->Server {:port port}))
