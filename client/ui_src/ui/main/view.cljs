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
    [:div.message [:span (:message message)]]]])

(defn footer
  []
  [:footer.toolbar.toolbar-footer
   [:h1.title "Version: 0.1.0"]])

(defn chats-sidebar-pane-conversation
  [active-converstation conversation]
  [:li.list-group-item
   {:key (:id conversation)
    :on-click (actions/select-conversation conversation)
    :class (when (= (:id active-converstation) (:id conversation))
             "active")}
   [:img.img-circle.media-object.pull-left {:width "32px" :height "32px"}]
   [:div.media-body
    [:strong (:name conversation)]
    (when-let [description (:description conversation)]
      [:p description])]])

(defn chats-sidebar-pane
  [active-converstation conversation-list]
  [:div.pane.pane-sm.sidebar
   [:ul.list-group
    (into
     [:li.list-group-header
      [:input.form-control {:type :text}]]
     (map (partial chats-sidebar-pane-conversation active-converstation) conversation-list))]])

(defn chat-message-pane
  [current-conversation]
  [:div.pane
   [:div.message-table-wrapper
    [:table.messages-table
     (into
      [:tbody]
      (map
       (fn [m] [message->list-item m])
       (:messages current-conversation)))]]])

(defn render
  []
  (let [route (subscribe [:route-state])
        current-conversation (subscribe [:current-conversation])
        conversation-list (subscribe [:conversation-list])]
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
         [chats-sidebar-pane @current-conversation @conversation-list]
         [chat-message-pane @current-conversation]]]
       [footer]])))
