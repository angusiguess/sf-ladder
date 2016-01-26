(ns sf-ladder.api
  (:require [cemerick.friend :as friend]
            [cemerick.friend
             [workflows :as workflows]
             [credentials :as creds]]
            [sf-ladder.db :as db]
            [compojure.core :refer [routes GET context defroutes]]
            [compojure.route :as route]
            [datomic.api :as dt]
            [clojure.tools.logging :as log]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.nested-params :refer [wrap-nested-params]]
            [ring.middleware.session :refer [wrap-session]]
            [org.httpkit.server :as http]
            [ring.logger :refer [wrap-with-logger]]))

(defn get-creds [conn]
  (fn [email]
    (let [response (dt/q '[:find (pull ?id [:competitor/email :competitor/password {:competitor/roles [:db/ident]}]) .
                           :in $ ?email
                           :where [?id :competitor/email ?email]] (dt/db conn) email)
          {:keys [:competitor/email :competitor/password :competitor/roles]} response]
      (cond-> nil
        email (assoc :username email)
        password (assoc :password password)
        roles (assoc :roles (into #{} (map :db/ident roles)))))))

(defroutes user-routes
  (GET "/hello" [] "Hello!"))

(defroutes admin-routes
  (GET "/hello" [] "Hello!"))

(defroutes api-routes
  (context "/user" request
           (friend/wrap-authorize user-routes #{:competitor.roles/user}))
  (context "/admin" request
           (friend/wrap-authorize admin-routes #{:competitor.roles/admin}))
  (GET "/login" [req]
       "Redirred"))

(defn app [{:keys [connection] :as db}]
  (-> api-routes
      wrap-session
      wrap-keyword-params
      wrap-nested-params
      wrap-params))


(defn start [db]
  (let [shutdown (http/run-server (app db) {:port 4444})]))
