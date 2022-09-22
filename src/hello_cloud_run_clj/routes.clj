(ns hello-cloud-run-clj.routes
  "This namespace maps the application routes to the request handlers."
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [hello-cloud-run-clj.handlers :as h]))

(defroutes app-routes
  (GET "/" req (h/home-handler req))
  (GET "/exception" req (h/exception-handler req))
  ;; The not-found route MUST be the last one
  (route/not-found (h/not-found-handler)))