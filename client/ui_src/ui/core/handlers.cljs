(ns ui.core.handlers
  (:require [re-frame.core :refer [register-handler]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [ui.core.app-state :as app-state]))

(register-handler
  :initialize-db
  [default-middleware]
  (constantly (js->clj app-state/default-app-state)))
