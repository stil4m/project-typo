(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))


(register-sub
 :conversation-list
 (fn [db]
   (reaction (vals (get-in @db [:conversations])))))

(register-sub
 :current-conversation
 (fn [db]
   (reaction (when (:current-conversation @db)
               (get-in @db [:conversations (:current-conversation @db)])))))
