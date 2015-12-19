(ns ui.util.time
  (:require [cljs-time.coerce :as time-coerce]
            [cljs-time.core :as time-core]))

(defn timestamp->time
  [timestamp]
  (let [d (time-coerce/from-long timestamp)]
    (str (time-core/hour d) ":" (time-core/minute d))))