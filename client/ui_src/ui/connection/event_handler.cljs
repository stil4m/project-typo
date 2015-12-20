(ns ui.connection.event-handler
  (:require [adzerk.cljs-console :as log :include-macros true]
            [re-frame.core :refer [dispatch]]))

(defmulti event (fn [message] (:event message)))

(defmethod event :authentication
  [message]
  (if (:result (:data message))
    (dispatch [:authentication-complete])
    (dispatch [:authentication-failed])))

(defmethod event :channel-created [message]
  (dispatch [:created-channel (:data message)]))

(defmethod event :joined-channel [message]
  (log/info "Server event :joined-channel | ~{(:channel message)}")
  (dispatch [:joined-channel (:data message)]))

(defmethod event :new-message [message]
  (log/info "Received message ~{message}")
  (dispatch [:received-message (:data message)]))

(defmethod event :all-channels [message]
  (dispatch [:fetched-all-channels (:data message)]))

(defmethod event :heartbeat [message]
  (log/debug "Heartbeat..."))

(defmethod event :default [message]
  (log/warn "Unhandled event ~{message}"))
