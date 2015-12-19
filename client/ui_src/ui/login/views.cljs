(ns ui.login.views
  (:require [re-frame.core :refer [subscribe]]
            [ui.login.actions :as actions]))

(defn render
  []
  (let [login-form-state (subscribe [:login-form-state])
        connection-address (subscribe [:connection-address])]
    (fn []
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
           [:label [:strong "Full Name"]]
           [:input.form-control {:type :text
                                 :on-change actions/set-full-name
                                 :value (get-in @login-form-state [:data :full-name])
                                 :placeholder "Full Name"}]]
          [:div.form-group
           [:label [:strong "Server Address"]]
           [:input.form-control {:type :text
                                 :on-change actions/set-connection-address
                                 :value @connection-address
                                 :placeholder "Server Address"}]]
          [:div
           [:button.btn.btn-primary.pull-right
            {:type :submit}
            "Login"]]]]]])))