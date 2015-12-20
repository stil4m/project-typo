(defproject server "0.1.0-SNAPSHOT"
  :description "The chat client"
  :url "awsum.nl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [cheshire "5.5.0"]
                 [aleph "0.4.1-beta3"]
                 [clj-time "0.11.0"]
                 [org.clojure/tools.logging "0.3.1" :exclusions [org.clojure/clojure]]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [environ "1.0.1"]
                 [com.stuartsierra/component "0.3.1"]
                 [reloaded.repl "0.2.1"]
                 [prismatic/schema "1.0.4"]]
  :plugins [[lein-environ "1.0.1"]]
  :profiles {:dev {:repl-options {:init-ns user}
                   :source-paths ["dev"]}})
