(ns sf-ladder.api-test
  (:require [clojure.test :refer :all]
            [sf-ladder.api :as api]
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

(deftest get-creds-returns-credentials-map
  (is (= ((api/get-creds @db-conn) "admin@sf-ladder.com")
         {:username "admin@sf-ladder.com"
          :password "$2a$10$YEcLBsNDng55zk3bZZkP4eZVXtlVm8yDaFVYpiHAEaud0RbOE3qze"
          :roles #{:competitor.roles/admin :competitor.roles/user}})))

(deftest login-with-bad-login
  (println ((api/app {:connection @db-conn}) (mock/request :post "/login" {:username "test@icicles.com" :password "wamp"}))))

(deftest login-with-good-login
  (println ((api/app {:connection @db-conn}) (mock/request :post "/login" {:username "admin@sf-ladder.com"
                                                                           :password "dearprudence"}))))

(deftest do-i-keep-a-session
  ((api/app {:connection @db-conn}) (mock/request :post "login" {:username "admin@sf-ladder.com"
                                                                 :password "dearprudence"}))
  (println "Logged in:" ((api/app {:connection @db-conn}) (mock/request :get "user/hello"))))
