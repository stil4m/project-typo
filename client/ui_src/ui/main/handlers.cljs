(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]
            [ui.main.transitions :as transitions]
            [ui.core.handlers :refer [write]]))


(defn send-message-for-current-room
  [db [message]]
  (let [room (get-in db [:rooms (:current-room db)])]
    (write (get db :ws) {:action :message :room (:id room) :body message})))

(defn perform-room-join
  [db [room-info]]
  (if (contains? (set (:open-rooms db)) (:id (:joining-room db)))
    (dispatch [:joined-room room-info])
    (do
      (log/info "TODO | Join ROOM:")
      (log/info "TODO | - room ~{(:id room-info)}")
      (dispatch [:joined-room room-info]))))

(defn perform-room-leave
  [db [room]]
  (log/info "TODO | Leave ROOM:")
  (log/info "TODO | - room ~{(:id room)}"))


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
 :join-room
 [trim-v
  (after perform-room-join)]
 (fn [db [room-info]]
   (assoc db :joining-room room-info)))

(register-handler
 :joined-room
 [trim-v]
 transitions/set-as-current-room-and-add-to-open)

(register-handler
 :leave-room
 [trim-v
  (after perform-room-leave)]
 transitions/leave-room)
