(ns ui.settings.view
  (:require [re-frame.core :refer [subscribe]]
            [ui.util.routing :as routing]
            [ui.component.page-control :as page-control]))

(defn render
  []
  (let [route (subscribe [:route-state])]
    (fn []
      [:div.window
       [:div.toolbar.toolbar-header
        [:h1.title "Settings"]
        [:div.toolbar-actions
         [page-control/history-btn-group @route]]]])))