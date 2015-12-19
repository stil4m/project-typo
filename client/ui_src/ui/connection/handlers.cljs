
(ns ui.connection.handlers
  (:require [cognitect.transit :as t]
            [re-frame.core :refer [register-handler dispatch trim-v]]
            [adzerk.cljs-console :as log :include-macros true]))

(defn write [websocket message]
  (log/debug "Sending message ~{message}")
  (let [w (t/writer :json)]
    (.send websocket (t/write w message))))

(defn read [message]
  (let [r (t/reader :json)
        message (t/read r message)]
    (log/debug "Received message ~{message}")
    message))

(defmulti event (fn [message] (:event message)))

(defmethod event :channel-created [message]
  (log/info "Created channel ~{message}")
  (dispatch [:created-channel (:created-channel message)]))

(defmethod event :new-message [message]
  (log/info "Received message ~{message}")
  (dispatch [:received-message message]))

(defmethod event :heartbeat [message]
  (log/debug "Heartbeat..."))

(defmethod event :default [message]
  (log/warn "Unhandled event ~{message}"))

(register-handler
 :connect-to-server
 (fn [db]
   (let [websocket (new js/WebSocket (get-in db [:connection :address]))]
     (set! (.-onmessage websocket)
           (fn [e]
             (event (read (.-data e)))))

     (set! (.-onclose websocket)
           (fn [e]
             (log/info "Disconnected from server")))

     (set! (.-onopen websocket)
           (fn [e]
             (log/info "Connected to server")))

     (assoc-in db [:connection :ws] websocket))))

(defn ^:export send-message [channel message]
  (write (:ws (:connection @re-frame.db/app-db)) {:action :message :channel channel :body message}))

(register-handler
 :connection/update-address
 [trim-v]
 (fn [db [new-address]]
   (update-in db [:connection] assoc :address new-address)))
