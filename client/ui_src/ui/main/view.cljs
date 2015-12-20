(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.main.actions :as actions]
            [ui.components.messages :as messages]))

(defn channel-list
  [title current-channel items]
  (into
   [:ul.mb3.p0 {:style {:list-style :none}}
    [:li [:h1.h6.ml1.caps.light-blue.muted title]]]
   (doall (map
           (fn [item]
             [:li.lh2.h5.flex.rounded.mb05.hover-bg-dark-orange.pointer.light-blue.hover-white
              {:key (:id item)
               :class (when (= (:id item) (:id current-channel))
                        "bg-dark-overlay bold")
               :on-click (actions/select-channel item)}
              [:span.ml1.status [:i.material-icons
                                 {:class (if (pos? (:unread item))
                                           "orange"
                                           "white")}
                                 "lens"]]
              [:span.flex-auto.px1.truncate (:name item)]
              [:span.mr1.unread-messages.light-blue.muted.col-1.right-align (when (pos? (:unread item)) (:unread item))]])
           items))))

(defn contacts-side-bar
  [current-channel channels-state current-user]
  [:nav.flex.flex-none.flex-column.contacts-sidebar.bg-dark-blue {:style {:padding "5px"}}
   [:div.flex.flex-row
    [:button.mt3.mr1.ml1.flex-auto.h5.btn.btn-primary.dark-gray.bg-white.regular {:on-click (actions/create-channel "My room")} "+ New Chat"]]
   [:div.flex-auto.mt6
    [channel-list "Rooms"
     current-channel
     (:channels channels-state)]
    [channel-list "People"
     current-channel
     (:people channels-state)]]
   [:div.user-menu.h4.lh4.px2.flex
    [:span [:i.material-icons.flex-center {:class "green"} "lens"]]
    [:span.name.px1.light-blue.flex-center (:full-name current-user)]
    [:span.options.light-blue.flex-center [:i.material-icons.grey "keyboard_arrow_down"]]]])

(defn message-box
  [channel]
  [:div.border-top.border-color-silver.ml2.mr2
   [:div.mt2.mb2.flex.flex-row
    [:input.flex-auto.border.border-color-silver.rounded
     {:type :text
      :on-key-down (actions/handle-message-input-key-stroke (:current-message channel))
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
        channels-state (subscribe [:channels-state])
        current-user (subscribe [:current-user])]
    (fn []
      [:div.window.flex.flex-row
       [contacts-side-bar @current-channel @channels-state @current-user]
       [message-panel @current-channel]])))
;[:aside.flex.flex-none.operations-sidebar.bg-light-gray.border-left.border-color-silver]])))
