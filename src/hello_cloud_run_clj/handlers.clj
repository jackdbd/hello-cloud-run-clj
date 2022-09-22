(ns hello-cloud-run-clj.handlers
  (:require
   [taoensso.timbre :as timbre :refer [debug error]]))

(defn home-handler
  [req]
  (debug "home-handler req" req)
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body   "Hello world!"})

(defn not-found-handler
  []
  {:status 404
   :headers {"Content-Type" "text/plain"}
   :body   "Resource not found"})

(defn exception-handler
  [req]
  (error "exception-handler req" req)
  (throw (Exception. "this is a test exception")))
