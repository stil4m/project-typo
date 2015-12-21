(ns project-typo.users
  (:require [com.stuartsierra.component :as component]
            [project-typo.db :as db]
            [clojure.tools.logging :as log]))

(defn- update-user-data [{:keys [db]} user-data]
  (let [update-doc (assoc user-data :type :user :_id (:username user-data))
        res @(db/upsert db update-doc)]
    update-doc))

(defprotocol IUserService
  (update-user [this message]))

(defrecord UserService [db]
  IUserService
  (update-user [this message]
    (update-user-data this message)))


(defn create-user-service []
  (map->UserService {}))