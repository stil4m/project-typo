
(ns ui.connection.handlers
  (:require [cognitect.transit :as t]
            [re-frame.core :refer [register-handler dispatch trim-v]]
            [adzerk.cljs-console :as log :include-macros true]))

(defn write [websocket message]
  (log/info "Sending message ~{message}")
  (let [w (t/writer :json)]
    (.send websocket (t/write w message))))

(defn read [message]
  (let [r (t/reader :json)
        message (t/read r message)]
    (log/info "Received message ~{message}")
    message))

(defmulti event (fn [message] (:event message)))

(defmethod event :room-created [message]
  (log/info "Created room ~{message}")
  (dispatch [:created-room (:created-room message)]))

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
             (.log js/console "Disconnected from server")))

     (set! (.-onopen websocket)
           (fn [e]
             (write websocket {:action :create-room :room "My room"})
             (write websocket {:action :message :room "My room" :body "main"})))
     (assoc-in db [:connection :ws] websocket))))

(defn ^:export send-message [room message]
  (write (:ws (:connection @re-frame.db/app-db)) {:action :message :room room :body message}))

(register-handler
 :connection/update-address
 [trim-v]
 (fn [db [new-address]]
   (update-in db [:connection] assoc :address new-address)))
