(ns sf-ladder.db
  (:require [sf-ladder.domain.protocols :as p]
            [clojure.tools.logging :as log]
            [datomic.api :as d]))

(defrecord DatomicDB [uri schema initial-data connection]
  p/Component
  (start [this]
    (d/create-database uri)
    (log/info "Starting database connection.")
    (let [conn (d/connect uri)]
            @(d/transact conn schema)
            @(d/transact conn initial-data)
            (assoc this :connection conn)))
  (stop [this]
    (log/info "Stopping database connection.")
    this))

(defn new-db [uri]
  (DatomicDB. uri (read-string (slurp "data/schema.edn"))
              (read-string (slurp "data/initial.edn"))
              nil))
