(ns ui.main.view
  (:require [re-frame.core :refer [subscribe dispatch]]
            [secretary.core :as secretary]
            [ui.routes :as routes]))

(defn message->list-item
  [message]
  [:tr {:key (:time message)}                               ;TODO Must be an ID
   [:td
    [:span.time (str (js/Date. (:time message)))]
    [:span.user (:user message)]
    [:div.message [:span (:message message)]]]])

(defn render
  []
  (let [route (subscribe [:active-route])
        current-conversation (subscribe [:current-conversation])]
    (fn []
      (.log js/console "Component")
      (.log js/console (str @current-conversation))
      [:div.window
       [:div.toolbar.toolbar-header
        [:h1.title "Header with actions"]
        [:div.toolbar-actions
         [:div.btn-group
          [:button.btn.btn-default
           [:span.icon.icon-left-open-mini]]
          [:button.btn.btn-default
           [:span.icon.icon-right-open-mini ""]]]

         [:button.btn.btn-default
          [:span.icon.icon-home.icon-text]
          "Filters"]]]
       [:div.window-content
        [:div.pane-group
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
             [:p "Lorem ipssum dolor sit amet."]]]]]
         [:div.pane
          [:div.message-table-wrapper
           [:table.messages-table
            (into
              [:tbody]
              (map
                (fn [m] [message->list-item m])
                (:messages @current-conversation))) ]]]]]])))