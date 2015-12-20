(ns ui.main.actions
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [ui.util.events :as util]
            [ui.core.routes :as routes]
            [clojure.string :as str]))

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
