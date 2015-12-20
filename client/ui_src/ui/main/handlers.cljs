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

(defn perform-channel-create
  [db [channel]]
  (write (get-in db [:connection :ws]) (merge {:action :create-channel} (select-keys channel [:name]))))

(defn perform-channel-join
  [db [channel]]
  (write (get-in db [:connection :ws]) {:action :join-channel :channel (:id channel)}))

(defn perform-channel-leave
  [db [channel]]
  (write (get-in db [:connection :ws]) {:action :leave-channel :data {:channel (:id channel)}}))

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

;TODO We should add a joined-channels for the initial join
(register-handler
 :joined-channel
 [default-middleware]
 transitions/joined-channel)


(defn join-all-channels
  [_ [channels]]
  ;TODO Join Channels that were open the last time you were online
  #_(doall (map
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
 identity)


(register-handler
 :leave-channel
 [default-middleware
  (after perform-channel-leave)]
 transitions/leave-channel)

(register-handler
 :received-message
 [default-middleware]
 transitions/received-message)