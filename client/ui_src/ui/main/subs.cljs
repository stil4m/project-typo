(ns ui.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]))

(register-sub
 :current-user
 (fn [db]
   (reaction (:user @db))))

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
