(ns project-typo.channel
  (:require [project-typo.constants :as constants]
            [project-typo.db :as db]
            [rethinkdb.query :as r]
            [schema.core :as schema]))

(def ChannelSchema
  {:name schema/Str})

(defn- create-channel [conn channel]
  (schema/validate ChannelSchema channel)
  (-> (r/table constants/channels-table)
      (r/insert channel {:return-changes true})
      (r/run conn)
      :changes
      first
      :new_val))

(defn- list-channels [conn]
  (-> (r/table constants/channels-table)
      (r/run conn)))

(defprotocol Channel
  (list-all [this])
  (create [this channel]))

(defrecord ChannelService [db]
  Channel
  (list-all [{:keys [db]}]
    (list-channels (db/connect db)))
  (create [{:keys [db]} channel]
    (create-channel (db/connect db) channel)))

(defn create-channel-service []
  (map->ChannelService {}))
