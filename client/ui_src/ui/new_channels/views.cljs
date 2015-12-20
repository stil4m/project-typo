(ns ui.new-channels.views
  (:require [ui.components.avatar :refer [avatar]]
            [ui.new-channels.actions :as actions]))

(def rooms
  [{:name "Bier drinken"
    :participants 6}
   {:name "Bier drinken"
    :participants 6}])

(def people
  [{:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}])

(defn status-text
  [status]
  (case status
    :available [:span.green "Available"]
    :busy [:span.red "Busy"]
    :offline [:span.gray.muted "Offline"]))

(defn participants-text
  [n]
  [:span..muted.light (str n " participants")])

(defn person-item
  [person]
  [:div.flex.m05.p1.rounded.hover-bg-light-gray.hover-border.hover-border-color-silver.hover-border.pointer
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize (:full-name person)]
     [:div.gray.flex-auto.mt05.lh7-8 (status-text (:status person))]]]])

(defn room-item
  [room]
  [:div.flex.m05.p1.rounded.hover-bg-light-gray.hover-border.hover-border-color-silver.hover-border.pointer
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize (:name room)]
     [:div.gray.flex-auto.mt05.lh7-8 (participants-text (:participants room))]]]])

(defn tab-header
  [title]
  [:div.border-bottom.bottom-color-silver.mb1
   [:h2.h4.caps.dark-gray {:style {:letter-spacing ".5px"}} title]])

(defn people-tab
  [people]
  [:div.col-6.left
   [:div.px2
    [tab-header "People"]
    [:div.col-6.left
     [person-item {:username "matstijl"
                   :full-name "Mats Stijlaart"
                   :status :available}]]
    [:div.col-6.left
     [person-item {:username "matstijl"
                   :full-name "Mats Stijlaart"
                   :status :available}]]
    [:div.col-6.left
     [person-item {:username "matstijl"
                   :full-name "Mats Stijlaart"
                   :status :available}]]]])

(defn rooms-tab
  [rooms]
  [:div.col-6.left
   [:button.right.px3.mr2.btn.btn-primary.pull-right.bg-dark-orange
    {:type :submit
     :on-click actions/create-channel}
    "New Room"]
   (into [:div.px2 [tab-header "Rooms"]]
         (mapv
          (fn [room]
            [:div.col-6.left [room-item room]])
          rooms))])

(defn render
  []
  (fn []
    [:div.window.flex.flex-column.bg-white
     [:div.flex-none.mt4
      [:div.flex
       [:h1.h2.inline.col-2 "New chat"]
       [:input.col-4 {:type :text
                      :placeholder "Search people & rooms"}]
       [:div.flex-auto
        [:i.right.material-icons.dark-gray "close"]]]]
     [:div.flex-auto
      [:div {:style {:width "100%"}}
       [people-tab people]
       [rooms-tab rooms]]]]))