(ns ui.components.side-bar
  (:require [ui.util.routing :as route-util]
            [re-frame.core :refer [dispatch]]
            [ui.core.routes :as routes]))

(defn leave-channel
  [channel]
  (fn [e]
    (.preventDefault e)
    (.stopPropagation e)
    (dispatch [:leave-channel channel])))

(defn create-channel
  [name]
  (fn [e]
    (.preventDefault e)
    (route-util/set-route routes/new-channels)))

(defn select-channel
  [channel]
  (fn [e]
    (.preventDefault e)
    (dispatch [:set-active-channel channel])))

(defn close-channel-button
  [channel]
  [:div.inline-block.hover-show-child.mr05.light-blue.center.bg-white.circle.mt-nudge1
   {:on-click (leave-channel channel)
    :style {:line-height "15px" :width "19x" :height "14px"}}
   [:i.material-icons.dark-orange.mt-nudge2 {:style {:width "15px" :font-size "14x"}} "close"]])

(defn channel-list
  [title current-channel items]
  (into
   [:ul.mb3.p0 {:style {:list-style :none}}
    [:li [:h1.h6.ml1.caps.light-blue.muted title]]]
   (doall (map
           (fn [item]
             [:li.lh2.h5.flex.rounded.mb05.hover-bg-dark-orange.pointer.light-blue.hover-white.hover-parent
              {:key (:id item)
               :class (when (= (:id item) (:id current-channel))
                        "bg-dark-overlay bold")
               :on-click (select-channel item)}
              [:span.ml1.status [:i.material-icons
                                 {:class (if (pos? (:unread item))
                                           "orange"
                                           "white")}
                                 "lens"]]
              [:span.flex-auto.px1.truncate (:name item)]
              [:div.flex-none
               [:span.hover-hide-child.mr1.light-blue.muted.col-1.right-align (when (pos? (:unread item)) (:unread item))]
               [close-channel-button item]]])
           items))))

(defn side-bar
  [current-channel channels-state current-user]
  (.log js/console (str channels-state))
  [:nav.flex.flex-none.flex-column.contacts-sidebar.bg-dark-blue {:style {:padding "5px"}}
   [:div.flex.flex-row
    [:button.mt-specific-35.mr1.ml1.flex-auto.h5.btn.btn-primary.dark-gray.bg-light-gray {:on-click (create-channel "My room")} "New Chat"]]
   [:div.flex-auto.mt3
    [channel-list "Rooms"
     current-channel
     (:channels channels-state)]
    [channel-list "People"
     current-channel
     (:people channels-state)]]
   [:div.user-menu.h4.lh4.px2.flex
    [:span [:i.material-icons.flex-center {:class "green"} "lens"]]
    [:span.name.px1.light-blue.flex-center.truncate (:full-name current-user)]
    [:span.options.light-blue.flex-center [:i.material-icons.grey "keyboard_arrow_down"]]]])
