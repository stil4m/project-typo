(ns ui.connection.handlers
  (:require [cognitect.transit :as t]
            [re-frame.core :refer [register-handler trim-v]]))


:connection/update-address

(defn write [websocket message]
  (let [w (t/writer :json)]
    (.send websocket (t/write w message))))

(defn read [message]
  (let [r (t/reader :json)]
    (.log js/console (t/read r message))))

(register-handler
 :connect-to-server
 (fn [db]
   (let [websocket (new js/WebSocket (get-in db [:connection :address]))]
     (set! (.-onmessage websocket)
           (fn [e]
             (read (.-data e))))

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