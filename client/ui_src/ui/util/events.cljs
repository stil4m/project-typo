(ns ui.util.events)

(defn event->value
  [e]
  (.-value (.-target e)))