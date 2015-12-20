(ns ui.main.transitions
  (:require [cljs-uuid-utils.core :as uuid]))

(defn set-as-current-channel
  [db [channel]]
  (let [db-with-current (assoc db :current-channel (:id channel))]
    (if (get-in db-with-current [:channels (:id channel)])
      (assoc-in db-with-current [:channels (:id channel) :unread] 0)
      db)))

(defn add-current-message-to-queue
  [db [message-body]]
  (update-in db
             [:channels (:current-channel db)]
             (fn [channel]
               (-> (dissoc channel :current-message)
                   (update :queue conj {:client-id (str (uuid/make-random-uuid))
                                        :user (get-in db [:user :username])
                                        :body message-body})))))
