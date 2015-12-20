(ns ui.main.actions
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [ui.util.events :as util]
            [ui.util.routing :as route-util]
            [ui.core.routes :as routes]
            [clojure.string :as str]))

(defn create-channel
  [name]
  (fn [e]
    (.preventDefault e)
    (route-util/set-route routes/new-channels)))

(defn select-channel
  [channel]
  (fn [e]
    (.preventDefault e)
    (dispatch [:set-active-channel channel])))

(defn handle-message-input-key-stroke
  [message]
  (fn
    [e]
    (when (and
           (not (nil? message))
           (not (str/blank? message))
           (not (.-shiftKey e))
           (= (.-key e) "Enter"))
      (do
        (.preventDefault e)
        (dispatch [:send-current-message message])))))

(defn update-current-message
  [e]
  (.preventDefault e)
  (dispatch-sync [:update-current-message (util/event->value e)]))
