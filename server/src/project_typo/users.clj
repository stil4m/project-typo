(ns project-typo.users
  (:require [com.stuartsierra.component :as component]
            [project-typo.db :as db]
            [clojure.tools.logging :as log]
            [manifold.deferred :as d]))

(defn- update-user-data [{:keys [db]} user-data]
  (let [update-doc (assoc user-data :type :user :_id (:username user-data))
        res @(db/upsert db update-doc)]
    update-doc))

(defn result->user
  [res]
  {:username (get-in res [:value :username])
   :full-name (get-in res [:value :full-name])
   :status :available})                                     ;TODO Change to Actual status when persisted

(defn- perform-list-all
  [{:keys [db]}]
  @(d/chain (db/get-view db "all-users" {})
            #(get-in % [:body :rows])
            #(map result->user %)))

(defprotocol IUserService
  (update-user [this message])
  (list-all [this]))

(defrecord UserService [db]
  IUserService
  (update-user [this message]
    (update-user-data this message))
  (list-all [this]
    (perform-list-all this)))


(defn create-user-service []
  (map->UserService {}))