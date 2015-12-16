(ns ui.core.handlers
  (:require [re-frame.core :refer [register-handler
                                   path
                                   trim-v]]
            [ui.core.app-state :as app-state]))

(register-handler
  :initialize-db
  (fn [_]
    (js->clj app-state/default-app-state)))
