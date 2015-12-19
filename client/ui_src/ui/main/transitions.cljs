(ns ui.main.transitions)


(defn leave-room
  [db [room]]
  (assoc db :current-room (when (not= (:id room) (:current-room db))
                            (:current-room db))
            :open-rooms (filter #(not= % (:id room)) (:open-rooms db))))

(defn set-as-current-room-and-add-to-open
  [db [room]]
  (let [db-with-current (assoc db :current-room (:id room))]
    (if (contains? (set (:open-rooms db-with-current)) (:id room))
      db-with-current
      (assoc db-with-current :open-rooms (conj (:open-rooms db-with-current) (:id room))))))

(defn add-current-message-to-queue
  [db [message-body]]
  (-> (update-in db [:rooms (:current-room db)] dissoc :current-message)
      (update-in [:rooms (:current-room db) :messages] conj {:sending true
                                                             :user (get-in db [:user :username])
                                                             :body message-body})
      (update-in [:message-queue] conj {:room (:current-room db)
                                        :body message-body})))
