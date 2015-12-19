(ns project-typo.db
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [project-typo.constants :as constants]
            [rethinkdb.query :as r]))

(def not-contains? (complement contains?))

(defn get-dbs [conn]
  (into #{} (r/run (r/db-list) conn)))

(defn get-tables [conn]
  (into #{} (r/run (r/table-list) conn)))

(defn create-table [conn db table-name]
  (-> (r/db db)
      (r/table-create table-name)
      (r/run conn)))

(defn create-table-when-missing [conn db existing-tables table-name]
  (when (not-contains? existing-tables table-name)
    (log/info "Creating table:" table-name)
    (create-table conn db table-name)))

(defn ensure-db-exists! [conn db]
  (when (not-contains? (get-dbs conn) db)
    (log/info db "Does not exist yet, creating now")
    (r/run (r/db-create db) conn)))

(defn ensure-db-exists! [conn db]
  (when (not-contains? (get-dbs conn) db)
    (log/info db "Does not exist yet, creating now")
    (r/run (r/db-create db) conn)))

(defn initialize-db [conn db]
  (let [tables (get-tables conn)]
    (create-table-when-missing conn db tables constants/channels-table)
    (create-table-when-missing conn db tables constants/messages-table)
    (create-table-when-missing conn db tables constants/users-table)))

(defprotocol IDatabase
  (connect [this] "Method to create a connection to the database"))

(defrecord Database [host port token auth-key db]
  component/Lifecycle
  (start [component]
    (log/info "Starting Database..")
    (with-open [conn (connect component)]
      (ensure-db-exists! conn db)
      (initialize-db conn db))
    component)
  (stop [component]
    (log/info "Stopping Database..")
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
