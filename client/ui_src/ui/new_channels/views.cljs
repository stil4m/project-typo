(ns ui.new-channels.views
  (:require [ui.components.avatar :refer [avatar]]))


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
  [:div.flex
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize (:full-name person)]
     [:div.gray.flex-auto.mt05.lh7-8 (status-text (:status person))]]]])

(defn room-item
  [room]
  [:div.flex
   [avatar]
   [:div.flex-auto
    [:div.flex.flex-column
     [:div.dark-gray.bold.lh7-8.capitalize (:name room)]
     [:div.gray.flex-auto.mt05.lh7-8 (participants-text (:participants room))]]]])

(defn render
  []
  (fn []
    [:div.window.flex.flex-row.mt4
     [person-item {:username "matstijl"
                   :full-name "Mats Stijlaart"
                   :status :available}]
     [room-item {:name "Bier drinken"
                 :participants 6}]]))