(ns ui.login.views
  (:require [re-frame.core :refer [subscribe]]
            [ui.login.actions :as actions]))

(defn form-field
  [{:keys [title action value]}]
  [:div.mt2.mb1
   [:label.dark-gray [:strong title]]
   [:div.flex.flex-row
    [:input.flex-auto.form-control.border.rounded.border-color-silver.dark-gray
     {:type :text
      :on-change action
      :value value
      :placeholder title}]]])
(defn render
  []
  (let [login-form-state (subscribe [:login-form-state])
        connection-address (subscribe [:connection-address])]
    (fn []
      [:div.window.flex
       [:div.bg-dark-blue.flex-strech.col-12
        [:div.login-form-container.bg-white.p3.col-10.lg-col-3.md-col-4.sm-col-6.rounded.m4.mx-auto
         [:form {:on-submit actions/submit-form}

          [:h1.h2.center.dark-gray "Login"]
          [form-field {:title "Username"
                       :action actions/set-username
                       :value (get-in @login-form-state [:data :username])}]
          [form-field {:title "Full Name"
                       :action actions/set-full-name
                       :value (get-in @login-form-state [:data :full-name])}]
          [form-field {:title "Server Address"
                       :action actions/set-connection-address
                       :value @connection-address}]
          [:div
           [:button.btn.btn-primary.pull-right.bg-dark-orange
            {:type :submit}
            "Login"]]]]]])))
