(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [adzerk.cljs-console :as log :include-macros true]
            [ui.main.transitions :as transitions]
            [ui.connection.handlers :refer [write]]))

(defn has-channel-open?
  [db channel]
  (contains? (set (:open-channels db)) (:id channel)))

(defn send-message-for-current-channel
  [db []]
  (let [channel (get-in db [:channels (:current-channel db)])
        message (last (:queue channel))]
    (try
      (write (get-in db [:connection :ws]) (merge (select-keys message [:client-id :body])
                                                  {:action :message :channel (:id channel)}))
      (catch js/Object e
        (.log js/console (str "e " (js->clj e)))))))

(defn perform-channel-create
  [db [channel]]
  (write (get-in db [:connection :ws]) (merge {:action :create-channel} (select-keys channel [:name]))))

(defn perform-channel-join
  [db [channel]]
  (if (has-channel-open? db channel)
    (dispatch [:joined-channel channel])
    (do
      (write (get-in db [:connection :ws]) {:action :join-channel :channel (:id channel)})
      (dispatch [:joined-channel channel]))))

(defn perform-channel-leave
  [db [channel]]
  (write (get-in db [:connection :ws]) {:action :leave-channel :channel (:id channel)}))

(register-handler
 :set-active-channel
 [default-middleware]
 transitions/set-as-current-channel-and-add-to-open)

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

(register-handler
 :create-channel
 [default-middleware
  (after perform-channel-create)]
 identity)

(register-handler
 :created-channel
 [default-middleware
  (after (fn [db [created-channel]] (dispatch [:join-channel created-channel])))]
 transitions/add-created-channel)

(defn join-all-channels
  [_ [channels]]
  (doall (map
          #(dispatch [:join-channel %])
          channels)))

(register-handler
  :fetched-all-channels
  [default-middleware
   (after join-all-channels)]
  transitions/update-channels)

(register-handler
 :join-channel
 [default-middleware
  (after perform-channel-join)]
 (fn [db [channel-info]]
   (assoc db :joining-channel channel-info)))

(register-handler
 :joined-channel
 [default-middleware]
 transitions/set-as-current-channel-and-add-to-open)

(register-handler
 :leave-channel
 [default-middleware
  (after perform-channel-leave)]
 transitions/leave-channel)

(register-handler
 :received-message
 [default-middleware]
 transitions/received-message)