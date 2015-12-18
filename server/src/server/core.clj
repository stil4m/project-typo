(ns server.core
  (:require [aleph.http :as http]
            [manifold.stream :as stream]
            [clojure.tools.logging :as log]
            [byte-streams :as bs]
            [manifold.stream :as s]
            [manifold.deferred :as d]
            [manifold.bus :as bus]
            [environ.core :refer [env]]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


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
                (d/let-flow [room (s/take! conn)
                             name (s/take! conn)]
                            ;; take all messages from the chatroom, and feed them to the client
                            (s/connect
                             (bus/subscribe chatrooms room)
                             conn)
                            ;; take all messages from the client, prepend the name, and publish it to the room
                            (s/consume
                             #(bus/publish! chatrooms room %)
                             (->> conn
                                  (s/map #(str name ": " %))
                                  (s/buffer 100)))))))

(defn start-server []
  (let [port (Integer. (get env :port 5333))]
    (log/info "Started server on port" port)
    (http/start-server chat-handler {:port port})))

(defn stop-server [server]
  (.close server))

(bus/publish! chatrooms "main" "hoi!")

