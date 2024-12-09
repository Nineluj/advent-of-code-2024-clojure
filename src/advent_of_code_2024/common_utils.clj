(ns advent-of-code-2024.common-utils
  (:require [clojure.string :as str]))

(def input-dir "inputs")
(def sample-dir "samples")

(defn get-input [day-number test-data?]
  (let [dir (if test-data? sample-dir input-dir)
        path (str dir "/day" day-number ".txt")]
    (str/trim-newline (slurp path))))

(defn grid-get
  ([grid x y]
   (-> grid
       (nth y)
       (nth x)))
  ([grid [x y]]
   (grid-get grid x y)))

(defn vec-eq? [v0 v1]
  (= 0 (compare (vec v0) (vec v1))))

(defn vec2d-subtract [[x0 y0] [x1 y1]]
  [(- x1 x0) (- y1 y0)])

(defn vec2d-add [[x0 y0] [x1 y1]]
  [(+ x1 x0) (+ y1 y0)])

(defn sum [xs] (apply + xs))

(defn grid-transpose [grid]
  (apply mapv vector grid))

(defn grid-in-range?
  ([max-x max-y [x y]]
   (and
    (>= x 0)
    (< x max-x)
    (>= y 0)
    (< y max-y)))
  ([grid pos]
   (grid-in-range? (count (first grid))
                   (count grid)
                   pos)))
