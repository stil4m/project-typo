(ns project-typo.event-bus
  (:require [com.stuartsierra.component :as component]
            [manifold.bus :as bus]
            [manifold.stream :as s]))

(defn close-all-downstream [[_ v]]
  (dorun (map s/close! v)))

(defrecord EventBus [bus]
  component/Lifecycle
  (start [component]
    (assoc component :bus (bus/event-bus)))
  (stop [component]
    (dorun
     (map
      close-all-downstream
      (bus/topic->subscribers bus)))

    (dissoc component :bus)))

(defn new-event-bus []
  (map->EventBus {}))

