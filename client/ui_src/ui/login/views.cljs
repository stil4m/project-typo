(ns ui.login.views
  (:require [re-frame.core :refer [subscribe]]
            [ui.login.actions :as actions]))

(defn render
  []
  (let [login-form-state (subscribe [:login-form-state])]
    (fn []
      (.log js/console (str @login-form-state))
      [:div.window
       [:div.window-content.login-screen
        [:div.login-form-container
         [:form {:on-submit actions/submit-form}
          [:h1 "Login"]
          [:div.form-group
           [:label [:strong "Username"]]
           [:input.form-control {:type :text
                                 :on-change actions/set-username
                                 :value (get-in @login-form-state [:data :username])
                                 :placeholder "Username"}]]
          [:div.form-group
           [:label [:strong "Password"]]
           [:input.form-control {:type :password
                                 :on-change actions/set-password
                                 :value (get-in @login-form-state [:data :password])
                                 :placeholder "Password"}]]
          [:div
           [:button.btn.btn-primary.pull-right
            {:type :submit}
            "Login"]]]]]])))