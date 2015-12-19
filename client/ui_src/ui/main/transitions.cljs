(ns ui.main.transitions
  (:require [cljs-uuid-utils.core :as uuid]
            [adzerk.cljs-console :as log :include-macros true]))

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
  (update-in db
             [:channels (:current-channel db)]
             (fn [channel]
               (-> (dissoc channel :current-message)
                   (update :queue conj {:client-id (str (uuid/make-random-uuid))
                                        :user (get-in db [:user :username])
                                        :body message-body})))))

;; Handle received message
(defn increase-unread-when-not-active
  [db channel]
  (if (= (:current-channel db) (:id channel))
    channel
    (update channel :unread inc)))

(defn add-message-to-channel-messages
  [message channel]
  (update channel :messages conj message))

(defn remove-message-from-channel-queue
  [message channel]
  (assoc channel :queue (filterv
                         #(not= (:client-id %) (:client-id message))
                         (:queue channel))))

(defn received-message
  [db [message]]
  (update-in db [:channels (:channel message)]
             (fn [target-channel]
               (->> (increase-unread-when-not-active db target-channel)
                    (add-message-to-channel-messages message)
                    (remove-message-from-channel-queue message)))))

(defn enrich-channel
  [channel]
  (assoc channel :queue (vec (:queue channel))
                 :messages (vec (:messages channel))))

(defn add-created-channel
  [db [created-channel]]
  (update db :channels assoc (:id created-channel) (enrich-channel created-channel)))

(defn update-channels
  [db [channels]]
  (let [channels-by-id (into {} (map #(vector (:id %) (enrich-channel %)) channels))]
    (->
     (assoc db :channels channels-by-id)
     (assoc :open-channels (keys channels-by-id)))))
