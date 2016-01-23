(ns sf-ladder.core
  (:require [sf-ladder.db :as db]))

(def uri "datomic:dev://localhost:4334/sfladder")

(d/create-database uri)

(def conn (d/connect uri))

(def schema (read-string (slurp "schema.edn")))

(defn insert-user [name email psn-name elo]
  (let [user [{:db/id #db/id[:db.part/user]
               :competitor/user-name name
               :competitor/email email
               :competitor/psn-name psn-name
               :competitor/elo elo}]]
    @(d/transact conn user)))

(defn get-id-by-email [email]
  (let [results (d/q '[:find ?id .
                       :in $ ?email
                       :where [?id :competitor/email ?email]] (d/db conn) email)]
    results))

(defn update-elo [id new-elo]
  (let [user [{:db/id id
               :competitor/elo new-elo}]]
    @(d/transact conn user)))

(defn get-user-names []
  (let [results (d/q '[:find ?id ?competitor-name ?email ?psn-name ?elo
                       :where
                       [_ :competitor/user-name ?competitor-name]
                       [?id :competitor/email ?email]
                       [_ :competitor/psn-name ?psn-name]
                       [_ :competitor/elo ?elo]] (d/db conn))]
    results))

(defn -main [& args]
  (let [db-uri (first args)
        db (start (new-db uri))]))
