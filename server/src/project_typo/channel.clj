(ns project-typo.channel
  (:require [project-typo.constants :as constants]
            [project-typo.db :as db]
            [manifold.deferred :as d]
            [schema.core :as schema]
            [clojure.tools.logging :as log]))

(def ChannelSchema
  {:id schema/Str
   :name schema/Str
   :room schema/Bool
   :private schema/Bool
   :members [schema/Str]})

(def CreateChannelSchema
  {:name schema/Str
   :room schema/Bool
   :members [schema/Str]})

(defn result->channel [res]
  {:id (:id res)
   :name (get-in res [:value :name])
   :private (get-in res [:value :private])
   :room (get-in res [:value :room])
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
     :room (:room create-channel-data)
     :private true
     :members members}))

(defn user-can-access
  [user]
  #(or
    (not (:private %))
    (contains? (set (:members %)) user)))

(defn- list-channels [this user]
  @(d/chain (db/get-view (:db this) "all-channels" {})
            #(get-in % [:body :rows])
            #(map result->channel %)
            #(filter (user-can-access user) %)))                   ;TODO Solve in DB

(defprotocol Channel
  (list-all [this user])
  (create [this channel user]))

(defrecord ChannelService [db]
  Channel
  (list-all [this user]
    (list-channels this user))
  (create [this channel user]
    (create-channel this channel user)))

(defn create-channel-service []
  (map->ChannelService {}))

(comment
 (create
  (get-in reloaded.repl/system [:channel-service]) {:name "hoi"})
 (list-channels
  (get-in reloaded.repl/system [:channel-service])))
