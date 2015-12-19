(ns ui.settings.view
  (:require [re-frame.core :refer [subscribe]]
            [ui.util.routing :as routing]
            [ui.component.page-control :as page-control]
            [cljs-time.coerce :as time-coerce]
            [cljs-time.core :as time-core]))

(def sample-messages
  [{:id 1
    :user "tomtheun"
    :body "Hi"
    :time 1450041030000}
   {:id 2
    :user "mitkuijp"
    :body "Morning, had a good sleep?"
    :time 1450041040000}
   {:id 3
    :user "tomtheun"
    :body "Do you know where @stil4m is?"
    :time 1450041050000}
   {:id 4
    :user "stil4m"
    :body "Here I am"
    :time 1450041060000}
   {:id 5
    :user "stil4m"
    :body "Sorry I am late"
    :time 1450041070000}
   {:id 6
    :user "mitkuijp"
    :body "Always the same song :p"
    :time 1450041080000}
   {:id 7
    :user "tomtheun"
    :body "Ok guys, lets get to it. The plan for today is to build an awesome chat client. I hope you have brought your pyjamas, because it is going to be a long weekend."
    :time 1450041090000}
   {:id 8
    :user "stil4m"
    :body "Got them with me"
    :time 1450041100000}
   {:id 9
    :user "mitkuijp"
    :body "Yes"
    :time 1450041110000}
   {:id 10
    :user "tomtheun"
    :body "Who wants (beer)?"
    :time 1450041120000}
   {:id 11
    :user "stil4m"
    :body "(areyoukiddingme)"
    :time 1450041130000}
   {:id 12
    :user "mitkuijp"
    :body "Yep"
    :time 1450041140000}
   {:id 13
    :user "mitkujp"
    :body "s/Yep/Yes"
    :time 1450041150000}
   {:id 14
    :user "stil4m"
    :body "Ok, I'll will also have one, but only one."
    :time 1450041160000}
   {:id 15
    :user "tomtheun"
    :body "3 beers coming up"
    :time 1450041170000}])


(defn timestamp->time
  [timestamp]
  (let [d (time-coerce/from-long timestamp)]
    (str (time-core/hour d) ":" (time-core/minute d))))

(defn message-avatar
  []
  [:div.flex-none.circle.border.mr1.ml1.bg-darken-2 {:style {:height "32px" :width "32px"}} ])

(defn message-list
  [messages]
  (into [:div.flex-auto.mt3.mr3.ml3]
        (doall (map
                (fn [message]
                  [:div.h5.border-top.border-color-light-silver {:key (:id message)}

                   [:div.mt1.mb1.flex
                    [message-avatar]
                    [:div.flex-auto.mt-nudge1
                     [:div.flex.lh7-8
                      [:div.flex-auto.dark-gray (:user message)]
                      [:div.h6.gray.muted (timestamp->time (:time message))]]
                     [:div.mt05.gray (:body message)]]]])
                messages))))

(defn message-box
  []
  [:div.mr3.ml3.mt2.mb2.flex.flex-row
   [:input.flex-auto.border.border-color-silver.rounded {:type :text}]])

(defn room-list
  [title items]
  [:ul.mb3.px1 {:style {:list-style :none}}
   [:li [:h1.h6.px1.caps.light-blue.muted title]]
   (map
    (fn [item]
      [:li.lh2.h5.flex.px1.rounded.px1.mb05 {:key (:name item)
                                              :class (when (:active item) "bg-dark-overlay")}
       [:span.status [:i.material-icons
                      {:class (if (pos? (:unread item))
                                "orange"
                                "white")}
                      "lens"]]
       [:span.flex-auto.px1.truncate.light-blue (:name item)]
       [:span.unread-messages.light-blue.muted.col-3.right-align (when (pos? (:unread item)) (:unread item))]])
    items)])

(defn render
  []
  (let [route (subscribe [:route-state])]
    (fn []
      [:div.window.flex.flex-row
       [:nav.flex.flex-column.contacts-sidebar.bg-dark-blue
        [:div.flex-auto.mt6
         [room-list "Rooms"
          [{:name "TPL" :unread 1}
           {:name "Relations" :unread 23}
           {:name "Hackaton" :unread 0 :active true}]]
         [room-list "People"
          [{:name "TPL" :unread 1}
           {:name "Tomas 'Bier Koning' Theunissen" :unread 23}
           {:name "Mitchel 'De enorme kast, spierbundel, krijger-keizer' Kuijpers" :unread 0}]]]
        [:div.user-menu.h4.lh4.px2.flex
         [:span [:i.material-icons.flex-center {:class "green"} "lens"]]
         [:span.name.px1.light-blue.flex-center "Maarten Arts"]
         [:span.options.light-blue.flex-center [:i.material-icons.grey "keyboard_arrow_down"]]]]
       [:div.content.flex-auto.flex.flex-column.bg-white
        [:div.py2.bg-gray.bg-light-gray.flex.border-top.border-bottom.border-color-silver
         [:h1.h2.regular.flex-auto.dark-gray.m0.ml3 "Hackathon"]
         [:div.flex-none.mr2.flex.flex-column
          [:i.material-icons.dark-gray.flex-center.flex-none "search"]]]
        [message-list
         sample-messages]
        [message-box]]
       [:aside.flex.flex-none.operations-sidebar.bg-light-gray.border-left.border-color-silver]])))