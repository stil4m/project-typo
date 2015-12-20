(ns project-typo.transit
  (:require [cognitect.transit :as transit]
            [clj-time.coerce :as coerce])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(def ^:private joda-time-verbose-handler
  (transit/write-handler
   (constantly "t")
   (fn [v] (-> v coerce/to-date .getTime))
   (fn [v] (coerce/to-string v))))

(def ^:private joda-time-handler
  (transit/write-handler
   (constantly "m")
   (fn [v] (-> v coerce/to-date .getTime))
   (fn [v] (-> v coerce/to-date .getTime .toString))
   joda-time-verbose-handler))

(def custom-transit-handlers {org.joda.time.DateTime joda-time-handler})

(defn decode-message [msg]
  (let [in (ByteArrayInputStream. (.getBytes msg))
        reader (transit/reader in :json)]
    (transit/read reader)))

(defn encode-message [msg]
  (let [out (ByteArrayOutputStream. 4096)
        writer (transit/writer out :json {:handlers custom-transit-handlers})]
    (transit/write writer msg)
    (.toString out)))
