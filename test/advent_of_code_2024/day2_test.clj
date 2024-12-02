(ns advent-of-code-2024.day2-test
  (:require
   [advent-of-code-2024.core :as core]
   [advent-of-code-2024.day2 :as sut]
   [clojure.test :refer :all]))

(def example-input (core/get-input 2 true))

(deftest test-part-1-example
  (is (= (sut/part1 example-input) 2)))

(deftest test-part-2-example
  (is (= (sut/part2 example-input) 4)))
