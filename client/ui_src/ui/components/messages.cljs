(ns ui.components.messages
  (:require [ui.util.time :as time]))

(defn message-avatar
  []
  [:div.flex-none.circle.border.mr1.ml1.bg-darken-2 {:style {:height "32px" :width "32px"}} ])

(defn message-list
  [messages]
  [:div.flex-auto.flex-basis-scroll
   (into [:div.mt3.mr3.ml3]
         (doall (map
                 (fn [message]
                   [:div.h5.border-top.border-color-light-silver {:key (:id message)}

                    [:div.mt1.mb1.flex
                     [message-avatar]
                     [:div.flex-auto.mt-nudge1
                      [:div.flex.lh7-8
                       [:div.flex-auto.dark-gray (:user message)]
                       [:div.h6.gray.muted (time/timestamp->time (:time message))]]
                      [:div.mt05.gray (:body message)]]]])
                 messages)))])

(defn message-box
  []
  [:div.border-top.border-color-silver
   [:div.mr3.ml3.mt2.mb2.flex.flex-row
    [:input.flex-auto.border.border-color-silver.rounded {:type :text}]]])
