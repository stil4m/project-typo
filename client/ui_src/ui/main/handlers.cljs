(ns ui.main.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path]]
            [ui.core.typo-re-frame :refer [default-middleware do-write]]
            [adzerk.cljs-console :as log :include-macros true]
            [ui.main.transitions :as transitions]
            [ui.connection.handlers :refer [write-action]]
            [ui.connection.actions :as actions]))

(register-handler
 :set-active-channel
 [default-middleware]
 transitions/set-as-current-channel)

(register-handler
 :send-current-message
 [default-middleware
  (do-write #(actions/send-message (select-keys % [:client-id :body :channel])))]
 transitions/add-current-message-to-queue)

(register-handler
 :update-current-message
 [default-middleware]
 (fn [db [message]]
   (update-in db [:channels (:current-channel db)] assoc :current-message message)))
