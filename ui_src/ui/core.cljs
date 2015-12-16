(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [dispatch-sync
                                   subscribe]]
            [ui.app-state]
            [ui.handlers]
            [ui.subs]
            [ui.views :as views]
            [ui.routes :as routes]
            [secretary.core :as secretary]))

(enable-console-print!)

(defn root-component
  []
  (let [conversation (subscribe [:current-conversation])]
  (fn []
    [:div
     [:p.foo "hello world"]
     [:a {:on-click #(do
                      ;(.preventDefault %)
                      (secretary/dispatch! "/settings")
                      nil)}
      "Settings"]
     [:p (str @conversation)]])))

(defn render
  []
  (reagent/render [views/main-view]
    (.-body js/document)))

(defn ^:export init
  []
  (dispatch-sync [:initialize-db])
  (render))

(set! (.-onload js/window) init)