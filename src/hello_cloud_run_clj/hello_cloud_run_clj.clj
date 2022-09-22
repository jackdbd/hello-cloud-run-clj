(ns hello-cloud-run-clj.hello-cloud-run-clj
  (:gen-class)
  (:require
   [prone.middleware :as prone]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
   [taoensso.timbre :as timbre :refer [debug info]]
   [hello-cloud-run-clj.routes :refer [app-routes]])
  (:import
   [org.eclipse.jetty.server Server]))

(defn make-handler
  []
  (let [env (System/getenv "ENVIRONMENT")]
    (debug "Running in environment:" env)
    (if (= "development" env)
      (-> app-routes
          prone/wrap-exceptions
          (wrap-defaults api-defaults))
      (-> app-routes
          (wrap-defaults api-defaults)))))

;; Define a singleton that acts as the container for a Jetty server (note that
;; the Jetty Server is stateful: it can be started/stopped).
(defonce ^:private ^Server webserver (atom nil))

(defn start-server
  "Create a Jetty `Server`, run it, and store it in an atom.
  We use #'handler (which stands for (var handler)) to avoid stopping/restarting
  the jetty webserver every time we make changes to the Ring handler. We still
  have to refresh the browser though).
  We use `:join? false` to avoid blocking the thread while running the server
  (this is useful when running the application in the REPL)."
  [port]
  (info "Start Jetty server on port" port)
  (let [handler (make-handler)]
    (info "handler " handler)
    (reset! ^Server webserver
            (run-jetty handler {:host  "0.0.0.0"
                                :join? false
                                :port port}))))

(defn stop-server
  "Stop the Jetty `Server` held in the atom."
  []
  (info "Stop Jetty server")
  (.stop ^Server @webserver))

(defn -main
  "The application entry-point"
  [& _args]
  (let [port (Long/parseLong (System/getenv "PORT"))]
    (start-server port)))

(comment
  (start-server 3000)
  (stop-server))
