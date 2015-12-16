(ns ui.routing.transitions)

(defn go-back
  [db]
  {:active (first (:history db))
   :history (rest (:history db))
   :future (cons (:active db) (:future db))})

(defn go-forward
  [db]
  {:active (first (:future db))
   :future (rest (:future db))
   :history (cons (:active db) (:history db))})

(defn set-route
  [db route]
  (let [old-active (:active db)]
    (if (= old-active route)
      db
      {:active route
       :future []
       :history (if (seq (:active db))
                  (cons old-active (:history db)))})))