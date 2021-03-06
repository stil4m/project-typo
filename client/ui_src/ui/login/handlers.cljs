(ns ui.login.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [ui.util.websocket :refer [do-write]]
            [ui.util.routing :as routing]
            [ui.core.routes :as routes]
            [ui.connection.actions :as actions]))

(defn validate-login-data-for-submission
  [db]
  ;TODO Add validation
  (let [next-action (if (and (:username (:data db)) (:full-name (:data db)))
                      :login-submission-data-valid
                      :login-submission-data-invalid)]
    (dispatch [next-action])))


(register-handler
 :fetch-all-channels
 [default-middleware
  (do-write actions/list-channels)
  (do-write actions/list-people)]
 identity)


(register-handler
 :authentication-complete
 [default-middleware
  (path [:login-form])
  (after (fn [login-form] (dispatch [:login-success (assoc (:data login-form) :status :available)])))]
 (fn [db]
   (assoc db :authenticating false)))

(register-handler
 :authentication-failed
 [default-middleware
  (path [:login-form])]
 (fn [db]
   (assoc db :authenticating false)))


(register-handler
 :login-success
 [default-middleware
  (path [:user])
  (after (routing/set-route-fn routes/main))
  (after #(dispatch [:fetch-all-channels]))]
 (fn [_ [user]]
   user))

(register-handler
 :login-submission-data-valid
 [default-middleware
  (after #(dispatch [:connect-to-server]))]
 identity)                                                  ;TODO

  (register-handler
 :login-submission-data-invalid
 (fn [db]
   ;TODO UNSET LOADING
   ;TODO Add Error
   db))

(register-handler
 :login-form/submit
 [default-middleware
  (path [:login-form])
  (after validate-login-data-for-submission)]
 (fn [db]
   (assoc db :authenticating true)))

(register-handler
 :login-form/change
 [default-middleware (path [:login-form :data])]
 (fn [db [k v]]
   (assoc db k v)))
