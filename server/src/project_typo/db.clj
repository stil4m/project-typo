(ns project-typo.db
  (:require [aleph.http :as http]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [manifold.deferred :as d]
            [cheshire.core :as json]
            [project-typo.constants :as constants]))



(def views {:_id "_design/typo"
            :views {:all-channels
                    {:map "function(doc) {if (doc.name) {emit(doc._id, doc);}}"}
                    :by-channel
                    {:map "function(doc) {\n\tif (doc.channel && doc.time) {\n\t\tvar date = new Date(doc.time);\n\t\temit([date.getTime(), doc.channel], doc)\n\t}\n}\n"}}})

(def default-data-type "application/json")

(def configuration-defaults {:socket-timeout 0
                             :conn-timeout 5000
                             :follow-redirects true
                             :headers {:content-type "application/json"}
                             :as :json})


(defprotocol IDatabase
  (get-view [this view query-params] "get a view")
  (create [this doc] "Method to create a document"))

(defn create-views [uri]
  (http/put
   (str uri "/_design/typo")
   {:body (json/encode views)}))

(defn get-db-info [uri]
  (d/catch
      (http/get uri configuration-defaults)
      (fn [_] nil)))

(defn create-db [uri]
  (d/chain
   (http/put uri configuration-defaults)
   (fn [_] (create-views uri))))

(defn ensure-db [uri]
  (d/let-flow [db (get-db-info uri)]
              (if (nil? db)
                (create-db uri)
                db)))

(defrecord Database [host port token auth-key db]
  component/Lifecycle
  (start [component]
    (log/info "Starting Database..")
    (let [uri "http://127.0.0.1:5984/typpo"]
      (log/info @(ensure-db uri))
      (assoc component :uri uri)))
  (stop [component]
    (log/info "Stopping Database..")
    (dissoc component :uri))
  IDatabase

  (get-view [this view query-params]
    (http/get (str (:uri this) "/_design/typo/_view/" view)
              (merge
               configuration-defaults)))
  (create [this doc]
    (log/info this)
    (http/post (:uri this) (merge
                            configuration-defaults
                            {:body (json/encode doc)}))))

(defn create-database [config]
  (map->Database config))

(comment
  @(get-view (get-in reloaded.repl/system [:db]) "all-channels" {}))

