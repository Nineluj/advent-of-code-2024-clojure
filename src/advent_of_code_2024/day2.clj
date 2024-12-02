(ns advent-of-code-2024.day2
  (:require
   [clojure.edn :refer [read-string]]
   [instaparse.core :as insta]))

;; syntax: https://github.com/Engelberg/instaparse?tab=readme-ov-file#notation
(def number-pair-parser
  (insta/parser
   "document = line+
    line = number (<whitespace> number)* <newline?>
    number = #'-?\\d+'
    whitespace = #'\\s+'
    newline = '\n'"))

(defn parse-input [input]
  (let [parsed (number-pair-parser input)]
    (if (insta/failure? parsed)
      (throw (ex-info "Parsing failed"
                      {:error (insta/get-failure parsed)}))
      (->> parsed
           (insta/transform
            {:number clojure.edn/read-string
             :line vector
             :document vector})))))

(defn get-deltas [xs]
  (->> xs
       (partition 2 1)
       (map (fn [[x0 x1]] (- x1 x0)))))

(def safe-decrease #(<= 1 % 3))
(def safe-increase #(>= -1 % -3))

(defn safe-sequence? [nums]
  (or
   (every? safe-increase nums)
   (every? safe-decrease nums)))

(defn is-safe
  [nums]
  (->> nums
       get-deltas
       safe-sequence?))

(defn part1
  [inp]
  (->> inp
       parse-input
       (filter is-safe)
       count))

(defn drop-nth [n xs]
  (keep-indexed #(when (not= %1 n) %2) xs))

(defn part2 [inp]
  (->> inp
       parse-input
       (filter (fn [nums]
                 (some #(is-safe (drop-nth % nums))
                       (range (count nums)))))  ; Try removing each position
       count))
