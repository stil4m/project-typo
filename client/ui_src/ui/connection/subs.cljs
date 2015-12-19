(ns ui.connection.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub
                                   subscribe]]))

(register-sub
 :connection-address
 (fn [db]
   (reaction (get-in @db [:connection :address]))))
