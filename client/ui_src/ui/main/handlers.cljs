(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [adzerk.cljs-console :as log :include-macros true]
            [ui.main.transitions :as transitions]
            [ui.connection.handlers :refer [write]]))

(defn send-message-for-current-channel
  [db []]
  (let [channel (get-in db [:channels (:current-channel db)])
        message (last (:queue channel))]
    (try
      (write (get-in db [:connection :ws]) (merge (select-keys message [:client-id :body])
                                                  {:action :message :channel (:id channel)}))
      (catch js/Object e
        (.log js/console (str "e " (js->clj e)))))))

(register-handler
 :set-active-channel
 [default-middleware]
 transitions/set-as-current-channel)

(register-handler
 :send-current-message
 [default-middleware
  (after send-message-for-current-channel)]
 transitions/add-current-message-to-queue)

(register-handler
 :update-current-message
 [default-middleware]
 (fn [db [message]]
   (update-in db [:channels (:current-channel db)] assoc :current-message message)))
