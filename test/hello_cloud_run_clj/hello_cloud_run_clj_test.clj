(ns hello-cloud-run-clj.hello-cloud-run-clj-test
  (:require [clojure.test :as t :refer [deftest is testing]]))

(deftest hello-cloud-run-clj-test
  (testing "TODO: fix"
    (is (not= :foo :bar))))

(deftest failing-test
  (testing "TODO: fix test"
    (is (= 3 (+ 1 2)))))