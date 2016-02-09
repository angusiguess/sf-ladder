(ns sf-ladder.competitor-test
  (:require  [clojure.test :refer :all]
             [sf-ladder.entities.competitor :as c]
             [datomic.api :as d]))


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

(deftest get-creds-returns-credentials-map
  (println (c/get-profile @db-conn "admin@sf-ladder.com"))
  (println (c/update-profile @db-conn "admin@sf-ladder.com" {:competitor/user-name "bob"}))
  (println (c/get-profile @db-conn "admin@sf-ladder.com"))
  (is (= (c/get-profile @db-conn "admin@sf-ladder.com")
         {:competitor/user-name "admin"
          :competitor/email "admin@sf-ladder.com"
          :competitor/password "$2a$10$YEcLBsNDng55zk3bZZkP4eZVXtlVm8yDaFVYpiHAEaud0RbOE3qze"
          :competitor/roles [{:db/ident :competitor.roles/user}
                             {:db/ident :competitor.roles/admin}]})))
