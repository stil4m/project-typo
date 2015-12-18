(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))


(register-sub
 :conversation-list
 (fn [db]
   (reaction (map identity (get-in @db [:conversations])))))

(register-sub
 :current-conversation
 (fn [db]
   (reaction (let [conversation-id (:current-conversation @db)]
               (when conversation-id
                 (first (filter #(= (:id %) conversation-id) (:conversations @db))))))))
