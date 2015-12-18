(ns ui.routing.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub
                                   subscribe]]))

(register-sub
  :route-state
  (fn [db _]
    (reaction (:route @db))))

(register-sub
  :active-route
  (fn [db _]
    (let [route-state (subscribe [:route-state])]
      (reaction (:active @route-state)))))
