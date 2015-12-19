(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.main.actions :as actions]
            [ui.components.messages :as messages]))

(defn channel-list
  [title items]
  (into
   [:ul.mb3.px1 {:style {:list-style :none}}
    [:li [:h1.h6.px1.caps.light-blue.muted title]]]
   (doall (map
           (fn [item]
             [:li.lh2.h5.flex.px1.rounded.px1.mb05 {:key (:id item)
                                                    :class (when (:active item) "bg-dark-overlay")
                                                    :on-click (actions/select-channel item)}
              [:span.status [:i.material-icons
                             {:class (if (pos? (:unread item))
                                       "orange"
                                       "white")}
                             "lens"]]
              [:span.flex-auto.px1.truncate.light-blue (:name item)]
              [:span.unread-messages.light-blue.muted.col-3.right-align (when (pos? (:unread item)) (:unread item))]])
           items))))

(defn contacts-side-bar
  [channels-state]
  [:nav.flex.flex-none.flex-column.contacts-sidebar.bg-dark-blue
   [:div.flex-auto.mt6
    [channel-list "Channels"
     (:channels channels-state)]
    [channel-list "People"
     (:people channels-state)]]
   [:div.user-menu.h4.lh4.px2.flex
    [:span [:i.material-icons.flex-center {:class "green"} "lens"]]
    [:span.name.px1.light-blue.flex-center "Maarten Arts"]
    [:span.options.light-blue.flex-center [:i.material-icons.grey "keyboard_arrow_down"]]]])

(defn message-panel
  [current-channel]
  [:div.content.flex-auto.flex.flex-column.bg-white
   [:div.py2.bg-gray.bg-light-gray.flex.border-top.border-bottom.border-color-silver
    [:h1.h2.regular.flex-auto.dark-gray.m0.ml3 (:name current-channel)]
    [:div.flex-none.mr2.flex.flex-column
     [:i.material-icons.dark-gray.flex-center.flex-none "search"]]]
   [messages/message-list (:messages current-channel)]
   [messages/message-box]])
(defn render
  []
  (let [route (subscribe [:route-state])
        current-channel (subscribe [:current-channel])
        channels-state (subscribe [:channels-state])]
    (fn []
      [:div.window.flex.flex-row
       [contacts-side-bar @channels-state]
       [message-panel @current-channel]
       [:aside.flex.flex-none.operations-sidebar.bg-light-gray.border-left.border-color-silver]])))
