(ns ui.people.transitions)


(defn set-people
  [db [all-people-message]]
  (:people all-people-message))