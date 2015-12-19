(ns project-typo.server
  (:require [aleph.http :as http]
            [manifold.stream :as stream]
            [clojure.tools.logging :as log]
            [byte-streams :as bs]
            [manifold.stream :as s]
            [manifold.deferred :as d]
            [manifold.bus :as bus]
            [environ.core :refer [env]]
            [cognitect.transit :as transit]
            [com.stuartsierra.component :as component])
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

(defmulti action (fn [conn chatrooms m] (:action m)))

(defmethod action :create-room [conn chatrooms m]
  (s/connect
   (bus/subscribe chatrooms (:room m))
   conn)
  (log/info "created room" m))

(defmethod action :message [_ chatrooms {:keys [room body] :as msg}]
  (log/info "got message" msg)
  (bus/publish! chatrooms room (encode-message msg)))

(defmethod action :join-room [_ chatrooms m]
  (log/info "joined room" m))

(defmethod action :leave-room [_ chatrooms m]
  (log/info "left room" m))

(def non-websocket-request
  {:status 400
   :headers {"content-type" "application/text"}
   :body "Expected a websocket request."})

(defonce chatrooms (bus/event-bus))

(defn chat-handler
  [req]
  (d/let-flow [conn (d/catch
                        (http/websocket-connection req)
                        (fn [_] nil))]
              (if-not conn
                ;; if it wasn't a valid websocket handshake, return an error
                non-websocket-request
                ;; otherwise, take the first two messages, which give us the chatroom and name
                (d/let-flow [room (s/take! conn)]
                            (action conn chatrooms (decode-message room))
                            (s/consume
                             #(action conn chatrooms (decode-message %))
                             (->> conn
                                  (s/buffer 100)))))))

(defrecord Server [port server]
  component/Lifecycle
  (start [component]
    (log/info "Starting server on port " port)
    (assoc component :server (http/start-server chat-handler {:port port})))
  (stop [component]
    (log/info "Stopping server..")
    (when (:server component)
      (.close (:server component)))
    (dissoc component :server)))

(defn create-server [port]
  (map->Server {:port port}))
