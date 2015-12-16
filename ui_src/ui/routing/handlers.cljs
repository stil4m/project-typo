(ns ui.routing.handlers
  (:require [ui.routing.transitions :as transitions]
            [re-frame.core :refer [register-handler
                                   path
                                   trim-v]]))

(defn flat-apply-fn
  [f]
  (fn [db argv]
    (apply f db argv)))

(register-handler
  :routing/go-back
  [trim-v (path [:route])]
  transitions/go-back)

(register-handler
  :routing/go-forward
  [trim-v (path [:route])]
  transitions/go-forward)

(register-handler
  :routing/set-route
  [trim-v (path [:route])]
  (flat-apply-fn transitions/set-route))