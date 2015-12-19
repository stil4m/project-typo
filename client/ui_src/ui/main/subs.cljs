(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]))


(register-sub
 :channel-list
 (fn [db]
   (reaction (vals (get-in @db [:channels])))))

(register-sub
 :open-channels
 (fn [db]
   (reaction (:open-channels @db))))

(register-sub
 :channels-state
 (fn [db]
   (let [channel-list (subscribe [:channel-list])
         open-channels (subscribe [:open-channels])]
     (reaction {:channels (map #(get-in @db [:channels %]) @open-channels)
                :people ()}))))

(register-sub
 :current-channel
 (fn [db]
   (reaction (when (:current-channel @db)
               (get-in @db [:channels (:current-channel @db)])))))
