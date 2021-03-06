(defproject hello-electron "0.1.0-SNAPSHOT"
  :source-paths ["src"]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.189"]
                 [figwheel "0.5.0-2"]
                 [re-frame "0.6.0"]
                 [ring/ring-core "1.4.0"]
                 [adzerk/cljs-console "0.1.1" :scope "provided"]
                 [figwheel-sidecar "0.5.0-2"]
                 [com.andrewmcveigh/cljs-time "0.3.14"]
                 [com.lucasbradstreet/cljs-uuid-utils "1.0.2"]
                 [prismatic/schema "1.0.4"]
                 [com.cognitect/transit-cljs "0.8.232"]
                 [org.clojure/core.async "0.2.374"]]
  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-2"]
            [lein-less "1.7.5"]]

  :clean-targets ^{:protect false} ["resources/main.js"
                                    "resources/public/js/ui-core.js"
                                    "resources/public/js/ui-core.js.map"
                                    "resources/public/js/ui-out"]
  :cljsbuild
  {:builds
   [{:source-paths ["electron_src"]
     :id "electron-dev"
     :compiler {:output-to "resources/main.js"
                :optimizations :simple
                :pretty-print true
                :cache-analysis true}}
    {:source-paths ["ui_src" "dev_src"]
     :id "frontend-dev"
     :figwheel true
     :compiler {:output-to "resources/public/js/ui-core.js"
                :output-dir "resources/public/js/ui-out"
                :source-map true
                :asset-path "js/ui-out"
                :optimizations :none
                :cache-analysis true
                :recompile-dependents false
                :main "dev.core"}}
    {:source-paths ["electron_src"]
     :id "electron-release"
     :compiler {:output-to "resources/main.js"
                :optimizations :simple
                :pretty-print true
                :cache-analysis true}}
    {:source-paths ["ui_src"]
     :id "frontend-release"
     :compiler {:output-to "resources/public/js/ui-core.js"
                :output-dir "resources/public/js/ui-release-out"
                :source-map "resources/public/js/ui-core.js.map"
                :optimizations :simple
                :cache-analysis true
                :main "ui.core"}}]}

  :less {:source-paths ["ui_src/stylesheets"]
         :target-path "resources/public/css"}

  :figwheel {:http-server-root "public"
             :ring-handler tools.figwheel-middleware/app
             :css-dirs ["resources/public/css"]
             :server-port 3449})
