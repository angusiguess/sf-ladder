(defproject sf-ladder "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [crypto-password "0.1.3"]
                 [com.datomic/datomic-pro "0.9.5206" :exclusions [joda-time]]
                 [buddy "0.10.0"]
                 [ring-logger "0.7.5"]
                 [ring/ring-mock "0.3.0"]
                 [http-kit "2.1.18"]
                 [compojure "1.4.0"]]

  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :main sf-ladder.core)
