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
            {:number read-string
             :line vector
             :document vector})))))

(defn get-delta-between-elements [xs]
  (->> xs
       (partition 2 1)
       (map #(apply - %))
       (map #(* % -1))))

(def safe-decrease #(<= 1 % 3))
(def safe-increase #(>= -1 % -3))

(defn is-report-safe? [nums]
  (or
   (every? safe-increase nums)
   (every? safe-decrease nums)))

(defn is-safe
  [nums]
  (->> nums
       get-delta-between-elements
       is-report-safe?))

;; (defn is-safe-v2
;;   [nums]
;;   (let [xs (get-delta-between-elements nums)
;;         xs' (get-delta-between-elements xs)]
;;     xs'))

(defn part1
  [inp]
  (->> inp
       parse-input
       (filter is-safe)
       count))

(defn part2 [inp]
  nil)
