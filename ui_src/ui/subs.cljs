(ns ui.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :current-conversation
  (fn [db]
    (.log js/console "Got conversation")
    (reaction (:conversation @db))))

(register-sub
  :active-route
  (fn [db _]
    (reaction (get-in @db [:active-route]))))
