(ns ui.login.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub
                                   subscribe]]))

(register-sub
  :login-form-state
  (fn [db _]
    (reaction (:login-form @db))))
