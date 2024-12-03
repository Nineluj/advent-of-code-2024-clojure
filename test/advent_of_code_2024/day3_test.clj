(ns advent-of-code-2024.day3-test
  (:require
   [advent-of-code-2024.core :as core]
   [advent-of-code-2024.day3 :as sut]
   [clojure.test :refer :all]))

(def example-input (core/get-input 3 true))

(deftest test-part-1-example
  (is (= (sut/part1 example-input) 161)))

(def example-input-2 "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")

(deftest test-part-2-example
  (is (= (sut/part2 example-input-2) 48)))
