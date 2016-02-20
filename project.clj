(defproject sf-ladder "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :injections [(require 'pjstadig.humane-test-output)
               (pjstadig.humane-test-output/activate!)]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [fogus/ring-edn "0.3.0"]
                 [crypto-password "0.1.3"]
                 [pjstadig/humane-test-output "0.7.1"]
                 [com.datomic/datomic-pro "0.9.5206" :exclusions [joda-time]]
                 [buddy "0.10.0"]
                 [ring-logger "0.7.5"]
                 [ring/ring-mock "0.3.0"]
                 [http-kit "2.1.18"]
                 [compojure "1.4.0"]]

  :repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                   :creds :gpg}}
  :main sf-ladder.core)
