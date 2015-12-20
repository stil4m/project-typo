(ns ui.new-channels.actions
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [ui.core.routes :as routes]
            [ui.util.events :as util]
            [ui.util.routing :as route-util]))


(defn close
  [e]
  (.preventDefault e)
  (route-util/set-route routes/main))

(defn change-channel-filter
  [e]
  (.preventDefault e)
  (.log js/console (str "TODO | Change channel filter: " (util/event->value e)))
  nil)

(defn create-channel
  [e]
  (.preventDefault e)
  (dispatch [:create-channel {:name "A Room"}])
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
    (.log js/console (str "TODO | Selected room: " room))
    nil))