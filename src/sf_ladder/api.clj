(ns sf-ladder.api
  (:require [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [ring.middleware.edn :as edn]
            [crypto.password.scrypt :as password]
            [sf-ladder.db :as db]
            [sf-ladder.entities.competitor :as comp]
            [compojure.core :refer [routes GET context defroutes]]
            [compojure.route :as route]
            [datomic.api :as dt]
            [clojure.tools.logging :as log]
            [org.httpkit.server :as http]))

(defn if-authenticated [req handler]
  (if-not (authenticated? req)
    (throw-unauthorized)
    handler))

(defn make-auth-fn [conn]
  (fn [req user]
    (let [{:keys [username password]} user]
      (when-let [user-password (comp/get-profile conn username [:competitor/password])]
        (when (password/check password (:competitor/password user-password))
          username)))))

(defn home [req]
  (if-authenticated req {:status 200
                         :headers {"Content-Type" "application/edn"}
                         :body {:response "Hi There"}}))

(defn profile [conn req]
  (let [{:keys [identity]} req]
    (if-authenticated req
                      {:status 200
                       :headers {"Content-Type" "application/edn"}
                       :body (comp/get-profile conn identity)})))

(defn app [conn]
  (routes (GET "/" [] home)
          (GET "/profile" [] (partial profile conn))))

(defn basic-backend [conn]
  (http-basic-backend {:realm "sf-ladder"
                       :authfn (make-auth-fn conn)}))

(defn start [db]
  (let [{:keys [connection]} db
        handler (->  (app connection)
                    (wrap-authorization (basic-backend connection))
                    (wrap-authentication (basic-backend connection))
                    edn/wrap-edn-params)]
    (http/run-server handler {:port 8080})))
