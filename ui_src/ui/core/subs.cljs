(ns ui.core.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :current-conversation
  (fn [db]
    (reaction (:conversation @db))))