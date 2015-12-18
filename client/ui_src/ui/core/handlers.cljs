(ns ui.core.handlers
  (:require [re-frame.core :refer [register-handler
                                   path
                                   trim-v]]
            [ui.core.app-state :as app-state]
            [cognitect.transit :as t]))

(register-handler
  :initialize-db
  (fn [_]
    (js->clj app-state/default-app-state)))

(defn write [websocket message]
  (let [w (t/writer :json)]
    (.send websocket (t/write w message))))

(defn read [message]
  (let [r (t/reader :json)]
    (.log js/console (t/read r message))))

(register-handler
 :connect-to-server
 (fn [db]
   (let [websocket (new js/WebSocket "ws://10.31.1.21:5333")]
     (set! (.-onmessage websocket)
           (fn [e]
             (read (.-data e))))

     (set! (.-onopen websocket)
           (fn [e]
             (write websocket {:action :create-room :room "My room"})
             (write websocket {:action :message :room "My room" :body "main"})))
     (assoc db :ws websocket))))

(defn ^:export send-message [room message]
  (write (:ws @re-frame.db/app-db) {:action :message :room room :body message}))

(re-frame.core/dispatch [:connect-to-server])
