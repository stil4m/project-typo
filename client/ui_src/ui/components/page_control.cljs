(ns ui.component.page-control
  (:require [ui.util.routing :as routing]))

(defn history-btn-group
  [route-state]
  (let [has-history (seq (:history route-state))
        has-future (seq (:future route-state))]
    [:div.btn-group
     [:button.btn.btn-default {:class (when (not has-history)
                                        "disabled")
                               :on-click (when has-history
                                           routing/go-back)}
      [:span.icon.icon-left-open-mini]]
     [:button.btn.btn-default {:class (when (not has-future)
                                        "disabled")
                               :on-click (when has-future
                                           routing/go-forward)}
      [:span.icon.icon-right-open-mini]]]))