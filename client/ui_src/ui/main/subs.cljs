(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction run!]])
  (:require [re-frame.core :refer [register-sub subscribe]]))

(register-sub
 :channel-map
 (fn [db]
   (reaction (:channels @db))))

(register-sub
 :subscribed-channels
 (fn [db]
   (reaction (:subscribed-channels @db))))

(register-sub
 :open-channels
 (fn [db]
   (let [channel-map (subscribe [:channel-map])
         open-channels (subscribe [:subscribed-channels])]
     (reaction (mapv #(get @channel-map %) @open-channels)))))

(register-sub
 :open-rooms
 (fn [db]
   (let [open-channels (subscribe [:open-channels])]
     (reaction (filterv #(:room %) @open-channels)))))

(register-sub
 :open-conversations
 (fn [db]
   (let [open-channels (subscribe [:open-channels])]
     (reaction (filterv #(not (:room %)) @open-channels)))))

(register-sub
 :open-channels-state
 (fn [db]
   (let [room-channels (subscribe [:open-rooms])
         open-conversations (subscribe [:open-conversations])]
     (reaction {:channels @room-channels
                :people @open-conversations}))))

(register-sub
 :current-channel
 (fn [db]
   (reaction (when (:current-channel @db)
               (get-in @db [:channels (:current-channel @db)])))))

(register-sub
 :total-unread
 (fn [_]
   (let [open-rooms (subscribe [:open-rooms])]
     (reaction (reduce (fn [cnt v] (+ cnt (:unread v))) 0 @open-rooms)))))

(def total-unread (subscribe [:total-unread]))
(run!
  (let [electron (.require js/window "electron")]
    (.setBadge (.. electron -remote -app -dock) (if (= @total-unread 0) "" (str @total-unread)))))
