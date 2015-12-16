(ns dev.core
  (:require [figwheel.client :as fw :include-macros true]
            [ui.core :as core]
            [ui.core]))

(fw/watch-and-reload
 :websocket-url   "ws://localhost:3449/figwheel-ws"
 :jsload-callback (fn []
                    (core/render)
                    (print "reloaded")))
