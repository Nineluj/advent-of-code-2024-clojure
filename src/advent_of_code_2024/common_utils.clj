(ns advent-of-code-2024.common-utils
  (:require [clojure.string :as str]))

(def input-dir "inputs")
(def sample-dir "samples")

(defn get-input [day-number test-data?]
  (let [dir (if test-data? sample-dir input-dir)
        path (str dir "/day" day-number ".txt")]
    (str/trim-newline (slurp path))))

(defn grid-get [grid x y]
  (-> grid
      (nth y)
      (nth x)))

(defn vec-eq? [v0 v1]
  (= 0 (compare (vec v0) (vec v1))))

(defn sum [xs] (apply + xs))

(defn grid-transpose [grid]
  (apply mapv vector grid))

(defn grid-in-range? [max-x max-y [x y]]
  (and
   (>= x 0)
   (< x max-x)
   (>= y 0)
   (< y max-y)))