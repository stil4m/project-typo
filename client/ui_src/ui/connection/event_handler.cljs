(ns ui.connection.event-handler
  (:require [re-frame.core :refer [register-handler dispatch dispatch-sync after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [adzerk.cljs-console :as log :include-macros true]))

(defmulti event-handler (fn [message] (:event message)))

(defmethod event-handler :authentication
  [message]
  (if (:result (:data message))
    (dispatch [:authentication-complete])
    (dispatch [:authentication-failed])))

(defmethod event-handler :channel-created [message]
  (log/info "Server event-handler :channel-created | ~{message}")
  (dispatch [:created-channel (:data message)]))

(defmethod event-handler :joined-channel [message]
  (log/info "Server event :joined-channel | ~{(:channel message)}")
  (dispatch [:joined-channel (:data message)]))

(defmethod event-handler :new-message [message]
  (log/info "Received message ~{message}")
  (dispatch [:received-message (:data message)]))

(defmethod event-handler :all-channels [message]
  (dispatch [:fetched-all-channels (:data message)]))

(defmethod event-handler :all-people [message]
  (dispatch [:fetched-all-people (:data message)]))

(defmethod event-handler :heartbeat [message]
  (log/debug "Heartbeat..."))

(defmethod event-handler :default [message]
  (log/warn "Unhandled event ~{message}"))

(register-handler
 :handle-event
 [default-middleware]
 (fn [db [message]]
   (when-let [message-handler (get (:message-handlers db) (:message-id message))]
     (message-handler message))
   (event-handler message)
   (update db :message-handlers dissoc (:message-id message))))

(defn event
  [message]
  (dispatch-sync [:handle-event message]))