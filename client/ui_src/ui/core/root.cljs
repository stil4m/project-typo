(ns ui.core.root
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [dispatch-sync
                                   subscribe]]


   ;Core
            [ui.core.handlers]

   ;Login
            [ui.login.handlers]
            [ui.login.subs]

   ;Main
            [ui.main.handlers]
            [ui.main.subs]

   ;Routing
            [ui.routing.handlers]
            [ui.routing.subs]

   ;Connection
            [ui.connection.handlers]
            [ui.connection.subs]

   ;Channels
            [ui.channels.handlers]

   ;New Channels
            [ui.new-channels.subs]
            [ui.new-channels.handlers]

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
