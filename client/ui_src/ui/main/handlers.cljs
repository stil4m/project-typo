(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [adzerk.cljs-console :as log :include-macros true]
            [ui.main.transitions :as transitions]
            [ui.connection.handlers :refer [write]]))

(defn has-channel-open?
  [db channel]
  (contains? (set (:open-channels db)) (:id channel)))

(defn send-message-for-current-channel
  [db [message]]
  (let [channel (get-in db [:channels (:current-channel db)])]
    (write (get-in db [:connection :ws]) {:action :message :channel (:id channel) :body message})))

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
 [trim-v]
 transitions/set-as-current-channel-and-add-to-open)

(register-handler
 :send-current-message
 [trim-v
  (after send-message-for-current-channel)]
 transitions/add-current-message-to-queue)

(register-handler
 :update-current-message
 [trim-v]
 (fn [db [message]]
   (update-in db [:channels (:current-channel db)] assoc :current-message message)))

(register-handler
  :created-channel
  [trim-v
    (after (fn [db [message]] (dispatch [:join-channel message])))]
  (fn [db [message]]
    (update db :channels assoc (:id message) message)))

(register-handler
 :join-channel
 [trim-v
  (after perform-channel-join)]
 (fn [db [channel-info]]
   (assoc db :joining-channel channel-info)))

(register-handler
 :joined-channel
 [trim-v]
 transitions/set-as-current-channel-and-add-to-open)

(register-handler
 :leave-channel
 [trim-v
  (after perform-channel-leave)]
 transitions/leave-channel)
