(ns ui.components.messages
  (:require [ui.util.time :as time]
            [clojure.string :as string]))

(defn message-avatar
  []
  [:div.flex-none.circle.border.mr1.ml1.bg-darken-2 {:style {:height "32px" :width "32px"}}])

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
   (into [:div.mt3.mr3.ml3]
         (doall (map
                 (fn [message]
                   [:div.h5.border-top.border-color-light-silver {:key (message->key message)}
                    [:div.mt1.mb1.flex
                     [message-avatar]
                     [:div.flex-auto.mt-nudge1
                      [:div.flex.lh7-8
                       [:div.flex-auto.dark-gray (:user message)]
                       [:div.h6.gray.muted (if (nil? (:time message))
                                             "Sending..."
                                             (time/timestamp->time (:time message)))]]
                      [:div.mt05.gray (:body message)]]]])
                 messages)))])

