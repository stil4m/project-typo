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

(defn item
  [{:keys [header footer action]}]
  [:div.flex.m05.p1.rounded.hover-bg-light-gray.hover-border.hover-border-color-silver.hover-border.pointer
   {:on-click action}
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize.mt-nudge1 header]
     [:div.gray.flex-auto.mt05.lh7-8.h45 footer]]]])

(defn person-item
  [person]
  [item {:header (:full-name person)
         :footer (status-text (:status person))
         :action (actions/select-person person)}])

(defn room-item
  [room]
  [item {:header (:name room)
         :footer (participants-text (:participants room))
         :action (actions/select-room room)}])

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

(defn channels-column
  [title items]
  [:div.col-6.left {:style {:height "100%" :position :relative}}
   [:div.border-bottom.bottom-color-silver.mr1.ml1
    [:h2.h4.regular.dark-gray {:style {:letter-spacing ".5px"}} title]]
   [:div.mr1.ml1.overflow-scroll.absolute {:style {:top "45px" :right 0 :left 0 :bottom 0}}
    items]])

(defn render
  []
  (fn []
    (let [available-channels (subscribe [:available-channels])]
      (.log js/console (str "ABC"))
      (.log js/console (str @available-channels))
      [:div.bg-white {:style {:width "664px" :height "600px" :margin "100px auto" :padding "50px" :border "1px solid #ccc" :border-radius "5px"}}
       [:div.clearfix.ml1.mr1
        [:i.material-icons.small-icon.px05.absolute.gray.ml1.mt-nudge1 "search"]
        [:input.border.border-color-silver.rounded.left.col-5
         {:type :text
          :style {:padding-left "36px"}
          :on-change actions/change-channel-filter
          :placeholder "Search people & rooms"}]
        [:button.px3.btn.btn-primary.pull-right.bg-dark-blue.ls2.right.h5
         {:type :submit
          :on-click actions/create-channel}
         "New Room"]]
       [channels-column
        "People"
        [people-tab (:people @available-channels)]]
       [channels-column
        "Rooms"
        [rooms-tab (:rooms @available-channels)]]])))
