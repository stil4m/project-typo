(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]))


(defn listen-for-new-conversation
  [db [conversation]]
  (log/info "TODO | Start listen to messages on conversation: ~{(:id conversation)}"))

(register-handler
 :set-active-conversation
 [trim-v
  (after listen-for-new-conversation)]
 (fn [db [converstation]]
   (-> (assoc db :current-conversation (:id converstation))
       (assoc :open-conversations (conj (:open-conversations db) (:id converstation))))))
