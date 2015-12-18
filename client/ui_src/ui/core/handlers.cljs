(ns ui.core.handlers
  (:require [re-frame.core :refer [register-handler
                                   path
                                   trim-v]]
            [ui.core.app-state :as app-state]))

(register-handler
  :initialize-db
  (fn [_]
    (js->clj app-state/default-app-state)))

(register-handler
 :connect-to-server
 (fn [db]
   (let [websocket (new js/WebSocket "ws://localhost:5333")]
     (set! (.-onmessage websocket)
           (fn [e]
             (.log js/console e)))
     (set! (.-onopen websocket)
           (fn [e]
             (.send websocket "main")
             (.send websocket "mrtypo")))
     (assoc db :ws websocket))))

(defn ^:export send-message [message]
  (.send (:ws @re-frame.db/app-db) message))

(re-frame.core/dispatch [:connect-to-server])

