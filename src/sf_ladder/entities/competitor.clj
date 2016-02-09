(ns sf-ladder.entities.competitor
  (:require [datomic.api :as d]))

(defn get-profile [conn email]
  (into {} (d/q '[:find (pull ?id [:competitor/user-name
                                   :competitor/email
                                   :competitor/psn-name
                                   :competitor/elo
                                   :competitor/password
                                   {:competitor/roles [:db/ident]}
                                   :competitor/user]) .
                  :in $ ?email
                  :where [?id :competitor/email ?email]]
                (d/db conn) email)))


(defn update-profile [conn email args]
  (let [id (d/q '[:find ?competitor-id .
                  :in $ ?email
                  :where [?competitor-id :competitor/email ?email]] (d/db conn) email)
        transactions (map (fn [[k v]] [:db/add id k v]) args)]
    @(d/transact conn transactions)))

;; That's user editing. Next up: regions, login
