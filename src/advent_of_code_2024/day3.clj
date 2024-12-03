(ns advent-of-code-2024.day3
  (:require [clojure.edn]))

(defn parse-input [input]
  (->> input
       (re-seq #"mul\((\d+),(\d+)\)")
       (map #(drop 1 %))
       (map (fn [group]
              (map clojure.edn/read-string group)))))

(defn part1 [inp]
  (->> inp
       parse-input
       (map #(apply * %))
       (apply +)))

(defn part2 [inp] nil)

