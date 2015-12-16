(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.core.routes :as routes]
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

(defn chats-sidebar-pane
  []
  [:div.pane.pane-sm.sidebar
   [:ul.list-group
    [:li.list-group-header
     [:input.form-control {:type :text}]]
    [:li.list-group-item
     [:img.img-circle.media-object.pull-left {:width "32px" :height "32px"}]
     [:div.media-body
      [:strong "List item title"]
      [:p "Lorem ipssum dolor sit amet."]]]
    [:li.list-group-item
     [:img.img-circle.media-object.pull-left {:width "32px" :height "32px"}]
     [:div.media-body
      [:strong "List item title"]
      [:p "Lorem ipssum dolor sit amet."]]]
    [:li.list-group-item
     [:img.img-circle.media-object.pull-left {:width "32px" :height "32px"}]
     [:div.media-body
      [:strong "List item title"]
      [:p "Lorem ipssum dolor sit amet."]]]]])

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
        current-conversation (subscribe [:current-conversation])]
    (fn []
      [:div.window
       [:div.toolbar.toolbar-header
        [:h1.title "Main"]
        [:div.toolbar-actions
         [page-control/history-btn-group @route]
         [:button.btn.btn-default.pull-right
          {:on-click (routing/go-to-route-fn routes/settings)}
          [:span.icon.icon-cog.icon-text]
          "Settings"]]]
       [:div.window-content
        [:div.pane-group
         [chats-sidebar-pane]
         [chat-message-pane @current-conversation]]]
       [footer]])))