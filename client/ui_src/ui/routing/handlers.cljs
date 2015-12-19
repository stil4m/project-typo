(ns ui.routing.handlers
  (:require [ui.routing.transitions :as transitions]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [re-frame.core :refer [register-handler
                                   path]]))

(defn flat-apply-fn
  [f]
  (fn [db argv]
    (apply f db argv)))

(register-handler
  :routing/go-back
  [default-middleware (path [:route])]
  transitions/go-back)

(register-handler
  :routing/go-forward
  [default-middleware (path [:route])]
  transitions/go-forward)

(register-handler
  :routing/add-route
  [default-middleware (path [:route])]
  (flat-apply-fn transitions/add-route))

(register-handler
  :routing/set-route
  [default-middleware (path [:route])]
  (flat-apply-fn transitions/set-route))
