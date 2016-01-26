(ns sf-ladder.core
  (:require [sf-ladder.db :as db]
            [sf-ladder.domain.protocols :as p]
            [sf-ladder.api :as api]
            [clojure.tools.logging :as log]))

(defn -main [& args]
  (let [db-uri (first args)
        db (p/start (db/new-db db-uri))]
    (api/start db)))
