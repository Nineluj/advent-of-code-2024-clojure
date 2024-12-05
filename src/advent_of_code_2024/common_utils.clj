(ns advent-of-code-2024.common-utils)

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
