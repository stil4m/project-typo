(ns project-typo.messages
  (:require [project-typo.constants :as constants]
            [project-typo.db :as db]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [schema.core :as schema]))

(def MessageSchema
  {:body schema/Str
   :client-id schema/Uuid
   :channel schema/Str})

(defn- create-message [{:keys [db]} message]
  (let [time (coerce/to-long (time/now))
        res @(db/create
              db
              (assoc message :type :message
                             :time time))]

    {:id (get-in res [:body :id])
     :channel (:channel message)
     :client-id (:client-id message)
     :time time
     :user (:user message)
     :body (:body message)}))

(defn- most-recent-messages [{:keys [db]} channel amount]
  [])

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
