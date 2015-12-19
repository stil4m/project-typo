(ns ui.login.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after
                                   trim-v]]
            [ui.util.routing :as routing]
            [ui.core.routes :as routes]
            [ui.connection.handlers :refer [write]]))

(defn validate-login-data-for-submission
  [db]
  ;TODO Add validation
  (let [next-action (if (and (:username (:data db)) (:full-name (:data db)))
                      :login-submission-data-valid
                      :login-submission-data-invalid)]
    (dispatch [next-action])))


(register-handler
 :authentication-complete
 [trim-v
  (after (fn [db] (write (get-in db [:connection :ws]) {:action :list-channels})))
  (after #(dispatch [:login-success {:token "1234567890ABCDEF"
                                      :username "stil4m"
                                      :full-name "Mats Stijlaart"
                                      :description "Beer Fanatic"}]))]
 (fn [db]
   (assoc db :authenticating false)))

(register-handler
 :authentication-failed
 [trim-v]
 (fn [db]
   (assoc db :authenticating false)))


(register-handler
  :login-success
  [trim-v
   (path [:user])
   (after (routing/set-route-fn routes/main))]
  (fn [_ user]
    user))

(register-handler
  :login-submission-data-valid
  [trim-v
   (after #(dispatch [:connect-to-server]))]
  identity)                                                 ;TODO

(register-handler
  :login-submission-data-invalid
  (fn [db]
    ;TODO UNSET LOADING
    ;TODO Add Error
    db))

(register-handler
  :login-form/submit
  [trim-v
   (path [:login-form])
   (after validate-login-data-for-submission)]
  (fn [db]
    (assoc db :authenticating true)))

(register-handler
  :login-form/change
  [trim-v (path [:login-form :data])]
  (fn [db [k v]]
    (assoc db k v)))
