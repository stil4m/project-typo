(ns dev.core
  (:require [figwheel.client :as fw :include-macros true]
            [ui.core.root :as root]))

(fw/watch-and-reload
 :websocket-url   "ws://localhost:3449/figwheel-ws"
 :jsload-callback (fn []
                    (root/render)
                    (print "reloaded")))
