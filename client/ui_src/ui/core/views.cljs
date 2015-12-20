(ns ui.core.views
  (:require [re-frame.core :refer [subscribe]]
            [ui.main.view :as main]
            [ui.login.views :as login]
            [ui.new-channels.views :as new-channels]
            [ui.core.routes :as routes]
            [ui.util.routing :as routing]))

(defmulti views identity)

(defmethod views :main [_] main/render)
(defmethod views :login [_] login/render)
(defmethod views :new-channels [_] new-channels/render)


(defn route->view [{:keys [view query-params params]}]
  (let [result (reduce (fn [inner-view view]
                         (let [props {:query-params query-params
                                      :params params
                                      :current-view view}]
                           (if (nil? inner-view)
                             [(views view) props]
                             [(views view) (assoc props :inner-view inner-view)])))
                 nil
                 (reverse view))]
    result))

(defn main-view []
  (let [active-route (subscribe [:active-route])]
    (fn []
      (if @active-route
        (route->view @active-route)
        (routing/set-route routes/login)))))
