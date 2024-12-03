(ns advent-of-code-2024.day3
  (:require [clojure.edn]))

(defn parse-input [input]
  (->> input
       (re-seq #"(?:(mul)\((\d+),(\d+)\)|(don't\(\))|(do\(\)))")
       (keep (fn [match]
               (let [[_ mul x0 x1 dont do] match]
                 (cond
                   mul [(symbol 'mul) (read-string x0) (read-string x1)]
                   dont [(symbol 'do-not)]
                   do [(symbol 'do)]
                   :else nil))))
       vec))

(defn apply-instructions [always-do xs]
  (loop [in-do true
         xs xs
         acc 0]
    (if (empty? xs)
      acc
      (let [[op & args] (first xs)]
        (cond
          (= op 'do)
          (recur true (rest xs) acc)

          (= op 'do-not)
          (recur false (rest xs) acc)

          :else
          (recur
           in-do
           (rest xs)
           (if (or always-do in-do)
             (+ acc (apply * args))
             acc)))))))

(defn part1 [inp]
  (->> inp
       parse-input
       (#(apply-instructions true %))))

(defn part2 [inp]
  (->> inp
       parse-input
       (#(apply-instructions false %))))

