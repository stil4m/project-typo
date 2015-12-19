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

(r/now)

(defprotocol IMessageService
  (create [this m]))

(defrecord MessageService [db]
  IMessageService
  (create [this message]
    (create-message this message)))

(defn create-message-service []
  (map->MessageService {}))
