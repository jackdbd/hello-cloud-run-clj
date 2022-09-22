(ns hello-cloud-run-clj.hello-cloud-run-clj
  "FIXME: my new org.corfield.new/scratch project.")

(defn exec
  "Invoke me with clojure -X hello-cloud-run-clj.hello-cloud-run-clj/exec"
  [opts]
  (println "exec with" opts))

(defn -main
  "Invoke me with clojure -M -m hello-cloud-run-clj.hello-cloud-run-clj"
  [& args]
  (println "-main with" args))
