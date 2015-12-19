(ns project-typo.messages
  (:require [project-typo.constants :as constants]
            [project-typo.db :as db]
            [rethinkdb.query :as r]
            [schema.core :as schema]))

(def MessageSchema
  {:body schema/Str
   :client-id schema/Uuid
   :channel schema/Str})

(defn- create-message [{:keys [db]} message]
  (-> (r/table constants/messages-table)
      (r/insert (assoc message :time (r/now)) {:return-changes true})
      (r/run (db/connect db))
      :changes
      first
      :new_val))

(defn- most-recent-messages [{:keys [db]} channel amount]
  (-> (r/table constants/messages-table)
      (r/order-by "time")
      (r/limit amount)
      (r/run (db/connect db))))

(defprotocol IMessageService
  (most-recent [this channel amount])
  (create [this m]))

(defrecord MessageService [db]
  IMessageService
  (most-recent [this channel amount]
    (most-recent-messages this channel amount))
  (create [this message]
    (create-message this message)))

(defn create-message-service []
  (map->MessageService {}))
