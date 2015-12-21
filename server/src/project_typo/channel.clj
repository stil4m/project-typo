(ns project-typo.channel
  (:require [project-typo.constants :as constants]
            [project-typo.db :as db]
            [manifold.deferred :as d]
            [schema.core :as schema]
            [clojure.tools.logging :as log]))

(def CreateChannelSchema
  {:name schema/Str
   :room schema/Bool
   :members [schema/Str]})

(defn result->channel [res]
  {:id (:id res)
   :name (get-in res [:value :name])
   :private (get-in res [:value :private])
   :members (get-in res [:value :members])})

(defn- create-channel [this create-channel-data user]
  (schema/validate CreateChannelSchema create-channel-data)
  (let [members (vec (conj (set (:members create-channel-data)) user))
        res @(db/create
              (:db this)
              (assoc create-channel-data :type :channel
                                         :private true
                                         :members members))]
    {:id (get-in res [:body :id])
     :name (:name create-channel-data)
     :private true
     :members members}))

(defn- list-channels [this]
  @(d/chain (db/get-view (:db this) "all-channels" {})
            #(get-in % [:body :rows])
            #(map result->channel %)))

(defprotocol Channel
  (list-all [this])
  (create [this channel user]))

(defrecord ChannelService [db]
  Channel
  (list-all [this]
    (list-channels this))
  (create [this channel user]
    (create-channel this channel user)))

(defn create-channel-service []
  (map->ChannelService {}))

(comment
 (create
  (get-in reloaded.repl/system [:channel-service]) {:name "hoi"})
 (list-channels
  (get-in reloaded.repl/system [:channel-service])))
