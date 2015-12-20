(ns ui.new-channels.views
  (:require [re-frame.core :refer [subscribe]]
            [ui.components.avatar :refer [avatar]]
            [ui.new-channels.actions :as actions]))

(defn status-text
  [status]
  (case status
    :available [:span.green "Available"]
    :busy [:span.dark-orange "Busy"]
    :offline [:span.gray.muted "Offline"]))

(defn participants-text
  [n]
  [:span.muted.light (str n " participants")])

(defn person-item
  [person]
  [:div.flex.m05.p1.rounded.hover-bg-light-gray.hover-border.hover-border-color-silver.hover-border.pointer
   {:on-click (actions/select-person person)}
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize (:full-name person)]
     [:div.gray.flex-auto.mt05.lh7-8 (status-text (:status person))]]]])

(defn room-item
  [room]
  [:div.flex.m05.p1.rounded.hover-bg-light-gray.hover-border.hover-border-color-silver.hover-border.pointer
   {:on-click (actions/select-room room)}
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize (:name room)]
     [:div.gray.flex-auto.mt05.lh7-8 (participants-text (:participants room))]]]])

(defn people-tab
  [people]
  [:div
   (into [:div]
         (mapv
          (fn [person]
            [person-item person])
          people))])

(defn rooms-tab
  [rooms]
  [:div
   (into [:div]
         (mapv
          (fn [room]
            [:div [room-item room]])
          rooms))])

(defn render
  []
  (fn []
    (let [available-channels (subscribe [:available-channels])]
      (.log js/console (str "ABC"))
      (.log js/console (str @available-channels))
      [:div.bg-white {:style {:width "664px" :height "600px" :margin "100px auto" :padding "50px" :border "1px solid #ccc" :border-radius "5px"}}
        [:div.flex-auto.clearfix
          [:i.material-icons.small-icon.px05.absolute.gray.ml1.mt-nudge1 "search"]
          [:input.border.border-color-silver.rounded.left.col-5
            {:type :text
             :style {:padding-left "36px;"}
             :on-change actions/change-channel-filter
             :placeholder "Search people & rooms"}]
          [:button.px3.btn.btn-primary.pull-right.bg-dark-orange.ls2.right
           {:type :submit
            :on-click actions/create-channel}
           "New Room"]]
        [:div.flex.mt3
          [:div.flex-grow.mr3
            [:div.border-bottom.bottom-color-silver.ml15
             [:h2.h4.regular.dark-gray {:style {:letter-spacing ".5px"}} "People"]]]
          [:div.flex-grow.ml3
            [:div.border-bottom.bottom-color-silver.ml15
             [:h2.h4.regular.dark-gray {:style {:letter-spacing ".5px"}} "Rooms"]]]]
        [:div.flex
            {:style {:height "537px" :overflow-y "scroll"}}
          [:div.flex-grow.mr3
            [people-tab (:people @available-channels)]]
          [:div.flex-grow.ml3
            [rooms-tab (:rooms @available-channels)]]]])))
