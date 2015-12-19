(ns project-typo.system
(:require [com.stuartsierra.component :as component]
          [environ.core :refer [env]]
          [project-typo.db :as db]
          [project-typo.server :as server]))

(defn new-system [config]
  (component/system-map
   :server (server/create-server (:port config 5333))
   :db (db/create-database (:database config))))



