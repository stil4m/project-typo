(ns electron.core)

(def app            (js/require "app"))
(def browser-window (js/require "browser-window"))
(def crash-reporter (js/require "crash-reporter"))

(def main-window (atom nil))

(defn init-browser []
  (reset! main-window (browser-window.
                        (clj->js {:titleBarStyle "hidden"
                                  :width 800
                                  :height 600})))
  ; Path is relative to the compiled js file (main.js in our case)
  (.loadURL @main-window (str "file://" js/__dirname "/public/index.html"))
  (.on @main-window "closed" #(reset! main-window nil)))

(.start crash-reporter)

;Do not quit app on OS X when all windows are closed
(.on app "window-all-closed" #(when-not (= js/process.platform "darwin")
                                (.quit app)))

(.on app "ready" init-browser)
