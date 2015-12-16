(ns ui.settings.view
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn render
  []
  (let [route (subscribe [:active-route])]
    (fn []
      [:div
       [:h1 "Hello world"]
       [:p (str @route)]])))