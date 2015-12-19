(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]
            [ui.main.transitions :as transitions]
            [ui.connection.handlers :refer [write]]))

(defn has-channel-open?
  [db channel]
  (contains? (set (:open-rooms db)) (:id channel)))

(defn send-message-for-current-room
  [db [message]]
  (let [room (get-in db [:rooms (:current-room db)])]
    (write (get-in db [:connection :ws]) {:action :message :room (:id room) :body message})))

(defn perform-channel-join
  [db [channel]]
  (if (has-channel-open? db channel)
    (dispatch [:joined-room channel])
    (do
      (write (get-in db [:connection :ws]) {:action :join-room :room (:id channel)})
      (dispatch [:joined-room channel]))))

(defn perform-channel-leave
  [db [room]]
  (write (get-in db [:connection :ws]) {:action :leave-room :room (:id room)}))

(register-handler
 :set-active-room
 [trim-v]
 transitions/set-as-current-room-and-add-to-open)

(register-handler
 :send-current-message
 [trim-v
  (after send-message-for-current-room)]
 transitions/add-current-message-to-queue)

(register-handler
 :update-current-message
 [trim-v]
 (fn [db [message]]
   (update-in db [:rooms (:current-room db)] assoc :current-message message)))

(register-handler
  :created-room
  [trim-v
    (after (fn [db [message]] (dispatch [:join-room message])))]
  (fn [db [message]]
    (update db :rooms assoc (:id message) message)))

(register-handler
 :join-room
 [trim-v
  (after perform-channel-join)]
 (fn [db [room-info]]
   (assoc db :joining-room room-info)))

(register-handler
 :joined-room
 [trim-v]
 transitions/set-as-current-room-and-add-to-open)

(register-handler
 :leave-room
 [trim-v
  (after perform-channel-leave)]
 transitions/leave-room)
