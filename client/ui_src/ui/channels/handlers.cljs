(ns ui.channels.handlers
  (:require [re-frame.core :refer [register-handler dispatch after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [ui.util.websocket :refer [do-write]]
            [ui.channels.transitions :as transitions]
            [ui.connection.actions :as actions]))

(register-handler
 :create-channel
 [default-middleware
  (do-write #(actions/create-channel {:name (:name %)}))]
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


(register-handler
 :fetched-all-channels
 [default-middleware]
 transitions/setup-channels)

(register-handler
 :join-channel
 [default-middleware
  (do-write #(actions/join-channel {:channel (:id %)}))]
 identity)


(register-handler
 :leave-channel
 [default-middleware
  (do-write #(actions/leave-channel {:channel (:id %)}))]
 transitions/leave-channel)

(register-handler
 :received-message
 [default-middleware]
 transitions/received-message)