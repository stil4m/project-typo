(ns ui.main.actions
  (:require [re-frame.core :refer [dispatch]]))

(defn select-conversation
  [conversation]
  (fn [e]
    (.preventDefault e)
    (dispatch [:set-active-conversation conversation])))
