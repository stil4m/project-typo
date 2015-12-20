(ns ui.channels.handlers
  (:require [re-frame.core :refer [register-handler dispatch after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [ui.channels.transitions :as transitions]
            [ui.connection.handlers :refer [write]]))


;; Side Effects
(defn perform-channel-create
  [db [channel]]
  (write (get-in db [:connection :ws]) (merge {:action :create-channel} (select-keys channel [:name]))))

(defn perform-channel-join
  [db [channel]]
  (write (get-in db [:connection :ws]) {:action :join-channel :channel (:id channel)}))

(defn perform-channel-leave
  [db [channel]]
  (write (get-in db [:connection :ws]) {:action :leave-channel :data {:channel (:id channel)}}))



(register-handler
 :create-channel
 [default-middleware
  (after perform-channel-create)]
 identity)

(register-handler
 :created-channel
 [default-middleware
  (after (fn [db [created-channel]] (dispatch [:join-channel created-channel])))]
 transitions/add-created-channel)

;TODO We should add a joined-channels for the initial join
(register-handler
 :joined-channel
 [default-middleware]
 transitions/joined-channel)


(register-handler
 :fetched-all-channels
 [default-middleware]
 transitions/setup-channels)

(register-handler
 :join-channel
 [default-middleware
  (after perform-channel-join)]
 identity)


(register-handler
 :leave-channel
 [default-middleware
  (after perform-channel-leave)]
 transitions/leave-channel)

(register-handler
 :received-message
 [default-middleware]
 transitions/received-message)