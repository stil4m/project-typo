(ns ui.components.messages
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [adzerk.cljs-console :as log :include-macros true]
            [clojure.string :as string]
            [ui.util.time :as time]
            [ui.components.avatar :refer [avatar]]
            [cljs.core.async :as async :refer [>! <! put! chan alts! timeout]]))

(defn- debounce [in ms]
  (let [out (chan)]
    (go-loop [last-val nil]
      (let [val (if (nil? last-val) (<! in) last-val)
            timer (timeout ms)
            [new-val ch] (alts! [in timer])]
        (condp = ch
          timer (do (>! out val) (recur nil))
          in (recur new-val))))
    out))

(defn message->key
  [message]
  (string/join
   "_"
   (filter
    #(not (nil? %))
    [(:client-id message) (:id message) (:time message)])))

(defn is-at-bottom? [node]
  (let [parent node
        node (.-firstElementChild node)
        height (.-scrollHeight node)
        parent-height (.-clientHeight parent)
        parent-visible-top (.-scrollTop parent)]
    (= height (+ parent-visible-top parent-height))))

(defn message-list
  [_]
  (let [scroll-events (chan)
        debounced-scroll-events (debounce scroll-events 100)]
    (go-loop []
      (let [event (<! debounced-scroll-events)]
        (when event
          (log/info "at bottom = ~(is-at-bottom? (.-target event))")
          (recur))))
    (fn [messages]
      [:div.flex-auto.flex-basis-scroll
       {:on-scroll (fn [e]
                     (put! scroll-events (.-nativeEvent e))
                     nil)}
       (into [:div.m2]
             (doall (map-indexed
                     (fn [i message]
                       [:div.h4.py1.mb1.py2.rounded
                        {:key (message->key message)
                         :class (when (even? i) "bg-light-gray border border-color-light-silver")}
                        [:div.flex.px1
                         [avatar]
                         [:div.flex-auto
                          [:div.flex
                           [:div.dark-gray.bold.lh7-8.capitalize (:user message)]
                           [:div.gray.flex-auto.ml2.muted.lh7-8
                            (if (nil? (:time message))
                              "Sending..."
                              (time/timestamp->time (:time message)))]]
                          [:div.mt0.gray (:body message)]]]])
                     (reverse messages))))])))
