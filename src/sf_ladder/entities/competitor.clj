(ns sf-ladder.entities.competitor
  (:require [datomic.api :as d]))

(defn get-profile
  ([conn email]
   (get-profile conn email [:competitor/user-name
                            :competitor/email
                            :competitor/psn-name
                            :competitor/elo
                            :competitor/password
                            {:competitor/roles [:db/ident]}
                            :competitor/user]))
  ([conn email fields]
   (let [eid (d/q '[:find ?id .
                    :in $ ?email
                    :where [?id :competitor/email ?email]]
                  (d/db conn) email)
         results (d/pull (d/db conn) fields eid)]
     (d/pull (d/db conn) fields eid))))

(defn update-profile [conn email args]
  (let [id (d/q '[:find ?competitor-id .
                  :in $ ?email
                  :where [?competitor-id :competitor/email ?email]] (d/db conn) email)
        transactions (map (fn [[k v]] [:db/add id k v]) args)]
    @(d/transact conn transactions)))

;; That's user editing. Next up: regions, login
