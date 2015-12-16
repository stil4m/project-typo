(ns ui.core.root
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary]
            [re-frame.core :refer [dispatch-sync
                                   subscribe]]


   ;Core
            [ui.core.handlers]
            [ui.core.subs]

   ;Login
            [ui.login.handlers]
            [ui.login.subs]

   ;Routing
            [ui.routing.handlers]
            [ui.routing.subs]


            [ui.core.views :as views]
            [ui.core.routes :as routes]))

(enable-console-print!)

(defn render
  []
  (reagent/render [views/main-view]
    (.-body js/document)))

(defn ^:export init
  []
  (dispatch-sync [:initialize-db])
  (render))

(set! (.-onload js/window) init)