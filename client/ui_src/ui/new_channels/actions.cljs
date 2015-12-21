(ns ui.new-channels.actions
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [ui.core.routes :as routes]
            [ui.util.events :as util]
            [ui.util.time :as time]
            [ui.util.routing :as route-util]))

(defn close
  [e]
  (.preventDefault e)
  (route-util/set-route routes/main))

(defn change-channel-filter
  [e]
  (.preventDefault e)
  (dispatch [:new-channel/change-filter (util/event->value e)]))

(defn create-channel
  [e]
  (.preventDefault e)
  (dispatch [:create-channel {:name (str "Room " (time/timestamp->time (js/Date.)))
                              :room true
                              :members []}])
  (route-util/set-route routes/main))


(defn select-person
  [person]
  (fn [e]
    (.preventDefault e)
    (.log js/console (str "TODO | Selected person: " person))
    nil))

(defn select-room
  [room]
  (fn [e]
    (.preventDefault e)
    (route-util/set-route routes/main)
    (dispatch [:join-channel room])
    (dispatch [:set-active-channel room])))