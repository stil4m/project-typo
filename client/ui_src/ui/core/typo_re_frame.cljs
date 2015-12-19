(ns ui.core.typo-re-frame
  (:require [re-frame.core :refer [after debug trim-v dispatch register-handler]]
            [schema.core :as s]
            [ui.core.schema :as schema]))

(def default-middleware
  (comp
   trim-v
   debug
   (after (fn [new-db v] (dispatch [:validate-db new-db v])))))

(register-handler
 :validate-db
 [trim-v]
 (fn [db v]
   (try
     (s/validate schema/AppState db)
     (catch js/Object e
       (.error js/console (str e))))
   db))