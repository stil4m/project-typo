(ns ui.main.actions
  (:require [re-frame.core :refer [dispatch]]
            [ui.util.events :as util]))

(defn join-room
  [room]
  (fn [e]
    (.preventDefault e)
    (dispatch [:join-room room])))

(defn select-room
  [room]
  (fn [e]
    (.preventDefault e)
    (dispatch [:set-active-room room])))

(defn handle-message-input-key-stroke
  [message]
  (fn
    [e]
    (when (and
           (not (.-shiftKey e))
           (= (.-key e) "Enter"))
      (do
        (.preventDefault e)
        (dispatch [:send-current-message message])))))

(defn update-current-message
  [e]
  (.preventDefault e)
  (dispatch [:update-current-message (util/event->value e)]))