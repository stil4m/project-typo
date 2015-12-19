(ns project-typo.system
  (:require [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [manifold.bus :as bus]
            [project-typo.channel :as channel]
            [project-typo.db :as db]
            [project-typo.server :as server]))

(defn new-system [config]
  (->
   (component/system-map
    :channel-service (channel/create-channel-service)
    :server (server/create-server (:port config 5333))
    :event-bus (bus/event-bus)
    :db (db/create-database (:database config)))
   (component/system-using
    {:server [:channel-service :event-bus]
     :channel-service [:db]})))
