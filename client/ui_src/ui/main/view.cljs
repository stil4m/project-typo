(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.core.routes :as routes]
            [ui.main.actions :as actions]
            [ui.component.page-control :as page-control]
            [ui.util.routing :as routing]))

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

(defn chats-sidebar-pane-room
  [active-converstation action closable room]
  [:li.list-group-item
   {:key (:id room)
    :on-click (action room)
    :class (when (= (:id active-converstation) (:id room))
             "active")}
   (when closable [:a.dismiss-room.pull-right {:on-click (actions/leave-room room)}
                   [:span.icon.icon-cancel]])
   [:img.img-circle.media-object.pull-left {:width "32px" :height "32px"}]
   [:div.media-body
    [:strong (:name room)]
    (when-let [description (:description room)]
      [:p description])]])

(defn chats-sidebar-pane
  [active-converstation rooms-state]
  [:div.pane.pane-sm.sidebar
   [:ul.list-group
    (when (seq (:joinable rooms-state))
      [:li.list-group-header [:strong "Joinable:"]])
    (doall
     (map (partial chats-sidebar-pane-room active-converstation actions/join-room false) (:joinable rooms-state)))
    [:li.list-group-header [:strong "Joined:"]]
    (doall
     (map (partial chats-sidebar-pane-room active-converstation actions/select-room true) (:joined rooms-state)))]])

(defn chat-message-pane
  [current-room]
  ^{:class "chat-message-pane"}
  [:div.message-table-wrapper
   [:table.messages-table
    (into
     [:tbody]
     (map
      (fn [m] [message->list-item m])
      (:messages current-room)))]])

(defn message-input
  [conversatation]
  [:div {:class "message-input"}
   [:div.text-area-container
    [:textarea {:on-key-down (actions/handle-message-input-key-stroke (:current-message conversatation))
                :on-change actions/update-current-message
                :value (:current-message conversatation)
                :placeholder "Type your message here"}]]])

(defn render
  []
  (let [route (subscribe [:route-state])
        current-room (subscribe [:current-room])
        rooms-state (subscribe [:rooms-state])]
    (fn []
      [:div.window
       [:div.toolbar.toolbar-header
        [:h1.title "Main"]
        [:div.toolbar-actions
         [page-control/history-btn-group @route]
         [:button.btn.btn-default.pull-right
          {:on-click (routing/set-route-fn routes/login)}   ;TODO Should have logout action
          [:span.icon.icon-logout.icon-text]
          "Logout"]
         [:button.btn.btn-default.pull-right
          {:on-click (routing/nav-to-route-fn routes/settings)}
          [:span.icon.icon-cog.icon-text]
          "Settings"]]]
       [:div.window-content
        [:div.pane-group
         [chats-sidebar-pane @current-room @rooms-state]
         (when @current-room
           [:div.messages-container.pane
            [chat-message-pane @current-room]
            [message-input @current-room]])]]
       [footer]])))
