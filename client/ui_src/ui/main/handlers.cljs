(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]))


(defn listen-for-new-room
  [db [room]]
  (log/info "TODO | Start listen to messages on room: ~{(:id room)}"))

(defn send-message-for-current-room
  [db [message]]
  (let [room (get-in  db [:rooms (:current-room db)])]
    (log/info "TODO | Send message for room:")
    (log/info "TODO | - room: ~{(:id room)}")
    (log/info "TODO | - message: ~{message}")))

(register-handler
 :set-active-room
 [trim-v
  (after listen-for-new-room)]
 (fn [db [converstation]]
   (-> (assoc db :current-room (:id converstation))
       (assoc :open-rooms (conj (:open-rooms db) (:id converstation))))))

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
