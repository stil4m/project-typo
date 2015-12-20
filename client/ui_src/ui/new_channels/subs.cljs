(ns ui.new-channels.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]))


(register-sub
 :available-channels
 (fn [db]
   (reaction (mapv (fn [[k v]] v) (:channels @db)))))
