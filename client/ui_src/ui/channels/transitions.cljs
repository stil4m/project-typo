(ns ui.channels.transitions)


(defn enrich-channel
  [channel]
  (assoc channel :room (get channel :queue true)            ; TODO, temporarily added to be schema compliant (should be set when starting a room/conversation)
                 :unread 0
                 :queue []
                 :messages []))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Created Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn add-created-channel
  [db [created-channel]]
  (-> (update db :channels assoc (:id created-channel) (enrich-channel created-channel))
      (assoc :current-channel (:id created-channel))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Joined Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn add-to-subscribed-channels
  [db channel-id]
  (if (contains? (set (:subscribed-channels db)) channel-id)
    db
    (assoc db :subscribed-channels (conj (vec (:subscribed-channels db)) channel-id))))

(defn add-recent-messages
  [db most-recent-messages channel]
  (if (nil? (get-in db [:channels channel]))
    db
    (assoc-in db [:channels channel :messages] most-recent-messages)))

(defn joined-channel
  [db [{:keys [channel most-recent-messages]}]]
  (-> (add-to-subscribed-channels db channel)
      (add-recent-messages most-recent-messages channel)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Setup Channels
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn setup-channels
  [db [message-data]]
  (assoc db :channels (into {} (map #(vector (:id %) (enrich-channel %)) (:channels message-data)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Leave channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn leave-channel
  [db [channel]]
  (assoc db :current-channel (when (not= (:id channel) (:current-channel db))
                               (:current-channel db))
            :subscribed-channels (vec (filter #(not= % (:id channel)) (:subscribed-channels db)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Handle received message
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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
  [db [message-data]]
  (.log js/console (str message-data))
  (update-in db [:channels (:channel message-data)]
             (fn [target-channel]
               (->> (increase-unread-when-not-active db target-channel)
                    (add-message-to-channel-messages message-data)
                    (remove-message-from-channel-queue message-data)))))
