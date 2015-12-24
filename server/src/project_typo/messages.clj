(ns project-typo.messages
  (:require [clj-time.coerce :as coerce]
            [clj-time.core :as time]
            [clojure.string :as str]
            [project-typo.constants :as constants]
            [project-typo.db :as db]
            [manifold.deferred :as d]
            [schema.core :as schema]))

(def MessageSchema
  {:body schema/Str
   :client-id schema/Uuid
   :channel schema/Str})

(defn- create-message [{:keys [db]} message]
  (let [time (coerce/to-long (time/now))
        res @(db/create
              db
              (assoc message :type :message
                             :time time))]

    {:id (get-in res [:body :id])
     :channel (:channel message)
     :client-id (:client-id message)
     :time time
     :user (:user message)
     :body (:body message)}))

(defn quote-if-string [v]
  (if (string? v)
    (str "\"" v "\"")
    (str v)))

(defn vec-to-key [v]
  (str
   "["
   (str/join "," (map quote-if-string v))
   "]"))

(defn row->message [{:keys [value id]}]
  (-> (select-keys value [:channel :body :client-id :user :time])
      (assoc :id id)))

(defn- most-recent-messages [{:keys [db]} channel amount]
  @(d/chain (db/get-view db "by-channel" {:startkey (vec-to-key [channel {}])
                                          :endkey (vec-to-key [channel])
                                          :descending true
                                          :limit amount})
            #(get-in % [:body :rows])
            #(mapv row->message %)))

(defprotocol IMessageService
  (most-recent [this channel amount])
  (create [this m]))

(defrecord MessageService [db]
  IMessageService
  (most-recent [this channel amount]
    (most-recent-messages this channel amount))
  (create [this message]
    (create-message this message)))

(defn create-message-service []
  (map->MessageService {}))

(let [channel "foo"]
  (str [channel, {}]))

(comment
  (most-recent (get-in reloaded.repl/system [:message-service]) "3823138b461a3b12812e21ae6901fec6" 1))
