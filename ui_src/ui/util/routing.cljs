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

(defn nav-to-route
  [f]
  (dispatch [:routing/add-route (secretary/dispatch! (f))]))

(defn nav-to-route-fn
  [f]
  (fn [e]
    (.preventDefault e)
    (nav-to-route f)
    nil))

(defn set-route
  [f]
  (dispatch [:routing/set-route (secretary/dispatch! (f))]))

(defn set-route-fn
  [f]
  (fn [e]
    (when (and e (.-preventDefault e))
      (.preventDefault e))
    (set-route f)
    nil))
