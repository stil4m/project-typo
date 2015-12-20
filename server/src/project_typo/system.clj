(ns project-typo.system
  (:require [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [manifold.bus :as bus]
            [project-typo.channel :as channel]
            [project-typo.messages :as messages]
            [project-typo.db :as db]
            [clojure.tools.logging :as log]
            [project-typo.server :as server]))

(defonce
  register-default-uncaught-exception-handler
  (Thread/setDefaultUncaughtExceptionHandler
   (reify Thread$UncaughtExceptionHandler
     (uncaughtException [_ thread ex]
       (log/error ex "Uncaught exception on" (.getName thread))))))

(defn new-system [config]
  (->
   (component/system-map
    :channel-service (channel/create-channel-service)
    :message-service (messages/create-message-service)
    :server (server/create-server (:port config 5333))
    :event-bus (bus/event-bus)
    :db (db/create-database (:database config)))
   (component/system-using
    {:server [:channel-service :event-bus :message-service]
     :message-service [:db]
     :channel-service [:db]})))
