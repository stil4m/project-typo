(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.core.routes :as routes]
            [ui.main.actions :as actions]
            [ui.components.page-control :as page-control]
            [ui.util.routing :as routing]
            [ui.components.messages :as messages]))

(defn message->list-item
  [message]
  [:tr {:key (:time message)}                               ;TODO Must be an ID
   [:td
    [:span.time (str (js/Date. (:time message)))]
    [:span.user (:user message)]
    [:div.message [:span (:body message)]]]])

(defn footer
  []
  [:footer.toolbar.toolbar-footer
   [:h1.title "Version: 0.1.0"]])

(defn chats-sidebar-pane-channel
  [active-converstation action closable channel]
  [:li.list-group-item
   {:key (:id channel)
    :on-click (action channel)
    :class (when (= (:id active-converstation) (:id channel))
             "active")}
   (when closable [:a.dismiss-channel.pull-right {:on-click (actions/leave-channel channel)}
                   [:i.material-icons.black "clear"]])
   [:img.img-circle.media-object.pull-left {:width "32px" :height "32px"}]
   [:div.media-body
    [:strong (:name channel)]
    (when-let [description (:description channel)]
      [:p description])]])

(defn chats-sidebar-pane
  [active-converstation channels-state]
  [:div.pane.pane-sm.sidebar
   [:ul.list-group
    (when (seq (:joinable channels-state))
      [:li.list-group-header [:strong "Joinable:"]])
    (doall
     (map (partial chats-sidebar-pane-channel active-converstation actions/join-channel false) (:joinable channels-state)))
    [:li.list-group-header [:strong "Joined:"]]
    (doall
     (map (partial chats-sidebar-pane-channel active-converstation actions/select-channel true) (:joined channels-state)))]])

(defn chat-message-pane
  [current-channel]
  ^{:class "chat-message-pane"}
  [:div.message-table-wrapper
   [:table.messages-table
    (into
     [:tbody]
     (map
      (fn [m] [message->list-item m])
      (:messages current-channel)))]])

(defn message-input
  [conversatation]
  [:div {:class "message-input"}
   [:div.text-area-container
    [:textarea {:on-key-down (actions/handle-message-input-key-stroke (:current-message conversatation))
                :on-change actions/update-current-message
                :value (:current-message conversatation)
                :placeholder "Type your message here"}]]])

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
     (:people channels-state)]
    (when (seq (:channels channels-state))
      [channel-list "channels"
       (:channels channels-state)])
    (when (seq (:people channels-state))
      [channel-list "People"
       (:people channels-state)])]
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
