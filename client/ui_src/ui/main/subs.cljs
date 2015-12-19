(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]))


(register-sub
 :room-list
 (fn [db]
   (reaction (vals (get-in @db [:rooms])))))

(register-sub
 :open-rooms
 (fn [db]
   (reaction (:open-rooms @db))))

(register-sub
 :rooms-state
 (fn [db]
   (let [room-list (subscribe [:room-list])
         open-rooms (subscribe [:open-rooms])]
     (reaction {:joinable (filter #(not (contains? (set @open-rooms) (:id %))) @room-list)
                :joined (map #(get-in @db [:rooms %]) @open-rooms)}))))

(register-sub
 :current-room
 (fn [db]
   (reaction (when (:current-room @db)
               (get-in @db [:rooms (:current-room @db)])))))
