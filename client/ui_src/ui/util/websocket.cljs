(ns ui.util.websocket
  (:require [re-frame.core :refer [dispatch after]]
            [cognitect.transit :as t]
            [adzerk.cljs-console :as log :include-macros true]
            [cljs-uuid-utils.core :as uuid]))

(defn write [websocket message]
  ;(log/debug "Sending message ~{message}")
  (let [w (t/writer :json)]
    (.send websocket (t/write w message))))

(defn write-action
  [db action]
  (try
    (write (get-in db [:connection :ws]) action)
    (catch js/Object e
      (log/warn "Write action failed ~{(js->clj e)}"))))

(defn do-write
  ([f]
   (do-write f nil))
  ([f g]
   (after (fn [db [data]]
            (let [message-id (str (uuid/make-random-uuid))]
              (when g
                (dispatch [:connection/response-handler message-id g]))
              (dispatch [:connection/write-action (assoc (f data)
                                                    :message-id
                                                    message-id)]))))))