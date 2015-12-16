(ns ui.handlers
  (:require [re-frame.core :refer [register-handler
                                   trim-v]]
            [ui.app-state :as app-state]))

(register-handler
  :initialize-db
  (fn [db ]
    (js->clj app-state/default-app-state)))

(register-handler
  :set-route
  [trim-v]
  (fn [db [route]]
    (.log js/console (str route))
    (assoc db :active-route route)))