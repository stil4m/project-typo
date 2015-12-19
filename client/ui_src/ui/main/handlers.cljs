(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]))


(defn send-message-for-current-room
  [db [message]]
  (let [room (get-in  db [:rooms (:current-room db)])]
    (log/info "TODO | Send message for room:")
    (log/info "TODO | - room: ~{(:id room)}")
    (log/info "TODO | - message: ~{message}")))

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

(defn set-as-current-room-and-add-to-open
  [db room]
  (let [db-with-current (assoc db :current-room (:id room))]
    (if (contains? (set (:open-rooms db-with-current)) (:id room))
      db-with-current
      (assoc db-with-current :open-rooms (conj (:open-rooms db-with-current) (:id room))))))

(register-handler
 :set-active-room
 [trim-v]
 (fn [db [room]]
   (set-as-current-room-and-add-to-open db room)))

(register-handler
 :send-current-message
 [trim-v
  (after send-message-for-current-room)]
 (fn [db [message]]
   (-> (update-in db [:rooms (:current-room db)] dissoc :current-message)
       (update-in [:rooms (:current-room db) :messages] conj {:sending true
                                                                              :user (get-in db [:user :username])
                                                                              :message message})
       (update-in [:message-queue] conj {:room (:current-room db)
                                         :message message}))))

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
 (fn [db [room]]
   (set-as-current-room-and-add-to-open db room)))

(register-handler
 :leave-room
 [trim-v
  (after perform-room-leave)]
 (fn [db [room]]
   (assoc db :current-room (when (not= (:id room) (:current-room db))
                             (:current-room db))
             :open-rooms (filter #(not= % (:id room)) (:open-rooms db)))))