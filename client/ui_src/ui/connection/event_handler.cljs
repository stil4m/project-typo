(ns ui.connection.event-handler
  (:require [re-frame.core :refer [register-handler dispatch dispatch-sync after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [adzerk.cljs-console :as log :include-macros true]))

;(defmulti event-handler (fn [message] (:event message)))

(def event-mapping
  {:authentication #(if (:result %) :authentication-complete :authentication-failed)
   :channel-created (constantly :created-channel)
   :joined-channel (constantly :joined-channel)
   :new-message (constantly :received-message)
   :all-channels (constantly :fetched-all-channels)
   :all-people (constantly :fetched-all-people)
   :heartbeat (constantly nil)})

(defn event-handler
  [message is-response?]
  (log/info "Event handler")
  (let [contains-event? (get event-mapping (:event message))]
    (if (not contains-event?)
      (log/warn "Unhandled event ~{event-key}")
      (let [event-key ((get event-mapping (:event message)) (:data message))]
        (when event-key
          (do
            (log/info "Server event-handler ~{event-key} | ~{message}")
            (dispatch [event-key (:data message) is-response?])))))))

(register-handler
 :handle-event
 [default-middleware]
 (fn [db [message]]
   (let [is-response? (get (:message-handlers db) (:message-id message))]
     (event-handler message is-response?)
     (update db :message-handlers dissoc (:message-id message)))))

(defn event
  [message]
  (dispatch-sync [:handle-event message]))