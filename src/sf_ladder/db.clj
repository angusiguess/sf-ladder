(ns sf-ladder.db
  (:require [sf-ladder.domain.protocols :as p]
            [clojure.tools.logging :as log]
            [datomic.api :as d]))

(deftype DatomicDB [uri schema initial-data connection]
  p/Component
  (start [this]
    (log/info "Starting database connection.")
    (try (let [conn (d/connect uri)]
           @(d/transact c schema)
           @(d/transact c initial-data)
           (assoc this :connection c))
         (catch Exception e
           (log/error e "An exception occurred:"))))
  (stop [this]
    (log/info "Stopping database connection.")
    this))

(defn new-db [uri]
  (DatomicDB. uri (read-string (slurp "data/schema.edn"))
              (read-string (slurp "data/initial.edn"))
              nil))
