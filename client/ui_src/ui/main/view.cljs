(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.main.actions :as actions]
            [ui.components.side-bar :refer [side-bar]]
            [ui.components.messages :as messages]))

(defn message-box
  [channel]
  [:div.border-top.border-color-silver.ml2.mr2
   [:div.mt2.mb2.flex.flex-row
    [:input.flex-auto.border.border-color-silver.rounded
     {:type :text
      :on-key-down (actions/handle-message-input-key-stroke channel)
      :on-change actions/update-current-message
      :value (:current-message channel)
      :placeholder "Type your message here"}]
    [:i.material-icons.dark-gray.px1.mt05 "insert_emoticon"]]])


(defn message-panel
  [current-channel]
  (into [:div.content.flex-auto.flex.flex-column.bg-white.pt2]
        (when current-channel
          [[:div.py2.ml2.mr2.flex.border-bottom.border-color-silver
            [:h1.h2.regular.flex-auto.dark-gray.m0.ml1 (:name current-channel)]
            [:i.material-icons.dark-gray.px1.mt05 "search"]
            [:i.material-icons.dark-gray.px1.mt05 "info_outline"]]
           [messages/message-list (concat (:messages current-channel)
                                          (:queue current-channel))]
           [message-box current-channel]])))

(defn render
  []
  (let [route (subscribe [:route-state])
        current-channel (subscribe [:current-channel])
        channels-state (subscribe [:open-channels-state])
        current-user (subscribe [:current-user])]
    (fn []
      [:div.window.flex.flex-row
       [side-bar @current-channel @channels-state @current-user]
       [message-panel @current-channel]])))
;[:aside.flex.flex-none.operations-sidebar.bg-light-gray.border-left.border-color-silver]])))
