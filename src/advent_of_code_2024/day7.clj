(ns advent-of-code-2024.day7
  (:require [clojure.edn]
            [instaparse.core :as insta]
            [clojure.math.combinatorics :as combo]))

(def parser
  (insta/parser
   "sheet = (equation <'\n'>)+ equation
    equation = test-value <':'> operators
    operators = (<whitespace> number)+
    whitespace = #'\\s+'
    test-value = number
    number = #'-?\\d+'"))

(defn parse-input [input]
  (let [parsed (parser input)]
    (if (insta/failure? parsed)
      (throw (ex-info "Parsing failed"
                      {:error (insta/get-failure parsed)}))
      (->> parsed
           (insta/transform
            {:sheet vector
             :test-value identity
             :operators vector
             :equation (fn [test-val operators]
                         {:goal test-val :operators operators})
             :number clojure.edn/read-string})))))

(defn evaluate [numbers operators]
  (reduce (fn [acc [op n]]
            (op acc n))
          (first numbers)
          (map vector operators (rest numbers))))

(defn operator-combinations [n allowed]
  (apply combo/cartesian-product
         (repeat n allowed)))

(defn can-make-value? [allowed {:keys [goal operators]}]
  (let [ops-needed (dec (count operators))]
    (->> (operator-combinations ops-needed allowed)
         (some #(= goal (evaluate operators %))))))

(defn part1 [inp]
  (let [allowed-operators [#(+ %1 %2)
                           #(* %1 %2)]]
    (->> inp
         parse-input
         (filter #(can-make-value? allowed-operators %))
         (map :goal)
         (apply +))))

(defn part2 [inp]
  (let [allowed-operators [#(+ %1 %2)
                           #(* %1 %2)
                           #(clojure.edn/read-string (format "%d%d" %1 %2))]]
    (->> inp
         parse-input
         (filter #(can-make-value? allowed-operators %))
         (map :goal)
         (apply +))))
