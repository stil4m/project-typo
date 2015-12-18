(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))


(register-sub
 :room-list
 (fn [db]
   (reaction (vals (get-in @db [:rooms])))))

(register-sub
 :current-room
 (fn [db]
   (reaction (when (:current-room @db)
               (get-in @db [:rooms (:current-room @db)])))))
