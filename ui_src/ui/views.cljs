(ns ui.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [ui.settings.view :as settings]
            [ui.main.view :as main]
            [ui.routes :as routes]
            [secretary.core :as secretary]))

(defmulti views identity)

(defmethod views :settings [_] settings/render)
(defmethod views :main [_] main/render)


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
        (dispatch [:set-route (secretary/dispatch! (routes/main))])))))
