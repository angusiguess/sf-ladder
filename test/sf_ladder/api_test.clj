(ns sf-ladder.api-test
  (:require [clojure.test :refer :all]
            [sf-ladder.api :as api]
            [sf-ladder.entities.competitor :as comp]
            [datomic.api :as d]
            [ring.mock.request :as mock]))

(defonce db-conn (atom nil))

(defn test-db [data]
  (fn [f]
    (d/create-database "datomic:mem://test")
    (let [conn (d/connect "datomic:mem://test")]
      @(d/transact conn (read-string (slurp "data/schema.edn")))
      @(d/transact conn data)
      (swap! db-conn (fn [x] conn))
      (f))
    (d/delete-database "datomic:mem://test")))

(use-fixtures :once (test-db [{:db/id #db/id[db.part/user]
                               :competitor/user-name "admin"
                               :competitor/email "admin@sf-ladder.com"
                               :competitor/psn-name ""
                               :competitor/elo 1600
                               :competitor/roles [:competitor.roles/user :competitor.roles/admin]
                               :competitor/password "$2a$10$YEcLBsNDng55zk3bZZkP4eZVXtlVm8yDaFVYpiHAEaud0RbOE3qze"}]))

