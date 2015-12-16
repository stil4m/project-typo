(ns ui.login.actions
  (:require [re-frame.core :refer [dispatch]]))

(defn set-login-form-prop-for-event
  [e prop]
  (.preventDefault e)
  (dispatch [:login-form/change prop (.-value (.-target e))])
  nil)

(defn set-username
  [e]
  (set-login-form-prop-for-event e :username))

(defn set-password
  [e]
  (set-login-form-prop-for-event e :password))

(defn submit-form
  [e]
  (.preventDefault e)
  (dispatch [:login-form/submit])
  nil)