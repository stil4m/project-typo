(ns project-typo.users
  (:require [com.stuartsierra.component :as component]))


(defrecord UserService [db])

(defn create-user-service []
  (map->UserService {}))
