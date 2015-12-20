(ns ui.new-channels.actions
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [ui.core.routes :as routes]
            [ui.util.routing :as route-util]))


(defn close
  [e]
  (.preventDefault e)
  (route-util/set-route routes/main))

(defn create-channel
  [e]
  (.preventDefault e)
  (dispatch [:create-channel {:name "A Room"}])
  (route-util/set-route routes/main))