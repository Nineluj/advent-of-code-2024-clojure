;; sample file to copy
(ns advent-of-code-2024.day4
  (:require
   [clojure.string :as str]
   [advent-of-code-2024.helpers :as helpers]))

(defn >> [xs x y]
  (-> xs
      (nth y)
      (nth x)))

(defn sum [xs] (apply + xs))

(defn transpose [xs]
  (apply mapv vector xs))

(def search-offsets
  ;; Call transpose to separate the different directions
  ;; that are defined in the same map
  (transpose
   (map
    (fn [i]
      (let [-i (* i -1)]
        [;; N
         [0 i]
         ;; NE
         [i i]
         ;; E
         [i 0]
         ;; SE
         [i -i]
         ;; S
         [0 -i]
         ;; SW
         [-i -i]
         ;; W
         [-i 0]
         ;; NW
         [-i i]]))
    (range 4))))

(defn is-search-xmas [xs]
  (= 0 (compare (vec xs) [\X \M \A \S])))

(defn make-range-checker [max-x max-y]
  (fn [[x y]]
    (and
     (>= x 0)
     (< x max-x)
     (>= y 0)
     (< y max-y))))

(defn get-search-coordinates [x y offsets]
  (map
   (fn [[xd yd]] [(+ x xd) (+ y yd)])
   offsets))

(defn map-search-to-letters [grid search]
  (map
   (fn [[x y]]
     (>> grid x y))
   search))

(defn count-search-matches-starting-at-coordinate [grid in-range? [x y]]
  (->> search-offsets
       ;; get the coordinates of the searches starting here
       (map (partial get-search-coordinates x y))
       ;; remove any search that has an out of range coordinate
       (filter (fn [search] (not (some #(not (in-range? %)) search))))
       ;; get the underlying letters
       (map (partial map-search-to-letters grid))
       ;; filter out searches that aren't xmas
       (filter is-search-xmas)
       ;; count the number of matches
       count))

(defn count-matches [inp]
  (let [grid (map (comp vec seq) (str/split-lines inp))
        max-y (count grid)
        max-x (count (first grid))
        all-coordinates (for [x (range max-x)
                              y (range max-y)]
                          [x y])
        search-finder (partial
                       count-search-matches-starting-at-coordinate
                       grid
                       (make-range-checker max-x max-y))]
    (->> all-coordinates
         (map search-finder)
         sum)))

(defn part1 [inp]
  (count-matches inp))

(defn part2 [inp] nil)

;; (def sample (advent-of-code-2024.helpers/get-input 4 true))
;; (part1 sample)
