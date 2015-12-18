(ns ui.core.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:require [secretary.core :as secretary]))

(secretary/reset-routes!)
(secretary/set-config! :prefix "#!")

(defroute login "/login" {:as params}
  {:view [:login]
   :params params
   :query-params (:query-params params)})

(defroute main "/main" {:as params}
  {:view [:main]
   :params params
   :query-params (:query-params params)})

(defroute settings "/settings" {:as params}
  {:view [:settings]
   :params params
   :query-params (:query-params params)})