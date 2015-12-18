(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]))


(defn listen-for-new-conversation
  [db [conversation]]
  (log/info "TODO | Start listen to messages on conversation: ~{(:id conversation)}"))

(defn send-message-for-current-conversation
  [db [message]]
  (let [conversation (get-in  db [:conversations (:current-conversation db)])]
    (log/info "TODO | Send message for conversation:")
    (log/info "TODO | - conversation: ~{(:id conversation)}")
    (log/info "TODO | - message: ~{message}")))

(register-handler
 :set-active-conversation
 [trim-v
  (after listen-for-new-conversation)]
 (fn [db [converstation]]
   (-> (assoc db :current-conversation (:id converstation))
       (assoc :open-conversations (conj (:open-conversations db) (:id converstation))))))

(register-handler
 :send-current-message
 [trim-v
  (after send-message-for-current-conversation)]
 (fn [db [message]]
   (-> (update-in db [:conversations (:current-conversation db)] dissoc :current-message)
       (update-in [:conversations (:current-conversation db) :messages] conj {:sending true
                                                                              :user (get-in db [:user :username])
                                                                              :message message})
       (update-in [:message-queue] conj {:conversation (:current-conversation db)
                                         :message message}))))

(register-handler
 :update-current-message
 [trim-v]
 (fn [db [message]]
   (update-in db [:conversations (:current-conversation db)] assoc :current-message message)))