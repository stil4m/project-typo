(ns ui.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:require [re-frame.core :refer [dispatch
                                   dispatch-sync]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [goog.events])
  (:import
    [goog.history Html5History EventType]
    [goog History]))

(secretary/reset-routes!)

(secretary/set-config! :prefix "#!")

(defroute main "/main" {:as params}
  {:view [:main]
   :params params
   :query-params (:query-params params)})

(defroute settings "/settings" {:as params}
  {:view [:settings]
   :params params
   :query-params (:query-params params)})
