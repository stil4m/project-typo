(ns ui.main.transitions)


(defn leave-channel
  [db [channel]]
  (assoc db :current-channel (when (not= (:id channel) (:current-channel db))
                            (:current-channel db))
            :open-channels (vec (filter #(not= % (:id channel)) (:open-channels db)))))

(defn set-as-current-channel-and-add-to-open
  [db [channel]]
  (let [db-with-current (assoc db :current-channel (:id channel))]
    (if (contains? (set (:open-channels db-with-current)) (:id channel))
      db-with-current
      (assoc db-with-current :open-channels (conj (:open-channels db-with-current) (:id channel))))))

(defn add-current-message-to-queue
  [db [message-body]]
  (-> (update-in db [:channels (:current-channel db)] dissoc :current-message)
      (update-in [:channels (:current-channel db) :messages] conj {:sending true
                                                             :user (get-in db [:user :username])
                                                             :body message-body})
      (update-in [:message-queue] conj {:channel (:current-channel db)
                                        :body message-body})))
