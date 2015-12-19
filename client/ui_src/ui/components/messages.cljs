(ns ui.components.messages
  (:require [ui.util.time :as time]
            [clojure.string :as string]))

(defn message-avatar
  []
  [:div.flex-none.circle.border.border-color-dark-gray.mr2.ml2.bg-light-gray {:style {:height "32px" :width "32px"}}])

(defn message->key
  [message]
  (string/join
   "_"
   (filter
    #(not (nil? %))
    [(:client-id message) (:id message) (:time message)])))

(defn message-list
  [messages]
  [:div.flex-auto.flex-basis-scroll
   (into [:div.m3]
         (doall (map-indexed
                 (fn [i message]
                   [:div.h4.py1.mb1.py2.rounded
                    {:key (message->key message)
                     :class (when (even? i) "bg-light-gray border border-color-light-silver")}
                    [:div.flex
                     [message-avatar]
                     [:div.flex-auto
                      [:div.flex
                       [:div.dark-gray.bold.lh7-8.capitalize (:user message)]
                       [:div.gray.flex-auto.ml2.muted.lh7-8
                        (if (nil? (:time message))
                          "Sending..."
                          (time/timestamp->time (:time message)))]]
                      [:div.mt0.mr2.gray (:body message)]]]])
                 messages)))])
