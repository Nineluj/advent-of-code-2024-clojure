(ns advent-of-code-2024.day1
  (:require
   [clojure.edn :refer [read-string]]
   [instaparse.core :as insta]
   [advent-of-code-2024.common-utils :as cu]))

;; syntax: https://github.com/Engelberg/instaparse?tab=readme-ov-file#notation
(def number-pair-parser
  (insta/parser
   "document = line+
    line = number <whitespace> number <newline?>
    number = #'-?\\d+'
    whitespace = #'\\s+'
    newline = '\n'"))

;; Parse the input
(defn parse-number-pairs [input]
  (let [parsed (number-pair-parser input)]
    (if (insta/failure? parsed)
      (throw (ex-info "Parsing failed"
                      {:error (insta/get-failure parsed)}))
      (->> parsed
           (insta/transform
            {:number read-string
             :line vector
             :document vector})))))

(defn get-manhattan-distance [list1 list2]
  (cu/sum
   (map #(abs (- %1 %2)) list1 list2)))

(defn extract-columns
  [pairs]
  (let [flattened-pairs (flatten pairs)
        first-column (take-nth 2 flattened-pairs)
        second-column (take-nth 2 (rest flattened-pairs))]
    [first-column second-column]))

(defn parse-input
  "To parse the input, we use insta to get the pair on each line.
  We then use the list of pairs to extract the two columns of numbers."
  [inp]
  (-> inp
      parse-number-pairs
      extract-columns))

(defn part1
  [inp]
  (let [[col1 col2] (parse-input inp)]
    (get-manhattan-distance (sort col1) (sort col2))))

(defn part2 [inp]
  (let [[col1 col2] (parse-input inp)
        freqs (frequencies col2)]
    (->> col1
         (map #(* %
                  ;; the last arg to get is the default value
                  (get freqs % 0)))
         cu/sum)))
