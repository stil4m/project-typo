(ns ui.util.time
  (:require [cljs-time.coerce :as time-coerce]
            [cljs-time.core :as time-core]
            [cljs-time.format :as format]))

(defn timestamp->time
  [timestamp]
  (format/unparse (format/formatters :hour-minute) (time-core/to-default-time-zone (time-coerce/from-long timestamp))))
