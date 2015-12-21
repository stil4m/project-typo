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
 :room-channels
 (fn [db]
   (let [channel-map (subscribe [:channel-map])
         open-channels (subscribe [:subscribed-channels])]
     (reaction (mapv #(get @channel-map %) @open-channels)))))

(register-sub
 :channels-state
 (fn [db]
   (let [room-channels (subscribe [:room-channels])]
     (reaction {:channels @room-channels
                :people ()}))))

(register-sub
 :current-channel
 (fn [db]
   (reaction (when (:current-channel @db)
               (get-in @db [:channels (:current-channel @db)])))))

(register-sub
 :total-unread
 (fn [_]
   (let [room-channels (subscribe [:room-channels])]
     (reaction (reduce (fn [cnt v] (+ cnt (:unread v))) 0 @room-channels)))))

(def total-unread (subscribe [:total-unread]))
(run!
  (let [electron (.require js/window "electron")]
    (.setBadge (.. electron -remote -app -dock) (if (= @total-unread 0) "" (str @total-unread)))))
