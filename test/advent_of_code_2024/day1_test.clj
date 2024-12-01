(ns advent-of-code-2024.day1-test
  (:require
   [advent-of-code-2024.core :as core]
   [advent-of-code-2024.day1 :as sut]
   [clojure.test :refer :all]))

(def example-input (core/get-input 1 true))

(deftest test-part-1-example
  (is (= (sut/part1 example-input) 11)))

(deftest test-part-2-example
  (is (= (sut/part2 example-input) 31)))
