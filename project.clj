(defproject sf-ladder "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [com.datomic/datomic-pro "0.9.5206" :exclusions [joda-time]]
                 [com.cemerick/friend "0.2.1"]
                 [ring-logger "0.7.5"]
                 [ring/ring-mock "0.3.0"]
                 [http-kit "2.1.18"]
                 [compojure "1.4.0"]]
  :main sf-ladder.core)
