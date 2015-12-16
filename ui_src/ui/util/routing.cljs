(ns ui.util.routing
  (:require [re-frame.core :refer [dispatch]]
            [secretary.core :as secretary]))


(defn- prevent-and-dispatch
  [e k]
  (.preventDefault e)
  (dispatch [k])
  nil)

(defn go-back
  [e]
  (prevent-and-dispatch e :routing/go-back))

(defn go-forward
  [e]
  (prevent-and-dispatch e :routing/go-forward)
  (.preventDefault e))

(defn go-to-route
  [f]
  (dispatch [:routing/set-route (secretary/dispatch! (f))]))

(defn go-to-route-fn
  [f]
  (fn [e]
    (.preventDefault e)
    (go-to-route f)
    nil))