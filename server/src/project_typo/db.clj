(ns project-typo.db
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [rethinkdb.query :as r]))

(defprotocol IDatabase
  (connect [this] "Method to create a connection to the database"))

(defrecord Database [host port token auth-key db]
  component/Lifecycle
  (start [component]
    (log/info "starting Database..")
    (connect component)
    component)
  (stop [component]
    (log/info "Stopping Datase..")
    component)
  IDatabase
  (connect [this]
    (r/connect :host host
               :port port
               :token token
               :auth-key auth-key
               :db db)))

(defn create-database [config]
  (map->Database config))
