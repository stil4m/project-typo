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
    [:div.window.bg-white
     [:div.right.mt2.right-align
      [:span.btn.material-icons.dark-gray.pointer
       {:on-click actions/close
        :style {:font-size "45px"}}
       "close"]]
     [:div.col-10.mx-auto
      [:div.flex.flex-column
       [:div.flex-none.mt3
        [:div.col-6
         [:div.px2
          [:div.col-6.left
           [:h1.h2.inline.col-2.dark-gray "New chat"]]
          [:div.col-6.left.flex
           [:input.col-4.flex-auto {:type :text
                          :placeholder "Search people & rooms"}]]]]]
       [:div.flex-auto
        [:div.mt2
         [people-tab people]
         [rooms-tab rooms]]]]]]))