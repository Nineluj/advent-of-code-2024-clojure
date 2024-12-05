;; sample file to copy
(ns advent-of-code-2024.day4
  (:require
   [clojure.string :as str]
   [advent-of-code-2024.common-utils :as cu]))

(defn get-search-coordinates [x y offsets]
  (map
   (fn [[xd yd]] [(+ x xd) (+ y yd)])
   offsets))

(defn map-search-to-letters [grid search]
  (map
   (fn [[x y]]
     (cu/grid-get grid x y))
   search))

(defn count-matches-around
  [grid
   in-range?
   search-offsets
   search-validator
   [x y]]
  (->> search-offsets
       ;; get the coordinates of the searches starting here
       (map (partial get-search-coordinates x y))
       ;; remove any search that has an out of range coordinate
       (filter (fn [search] (not (some #(not (in-range? %)) search))))
       ;; get the underlying letters
       (map (partial map-search-to-letters grid))
       ;; filter out searches that aren't xmas
       (filter search-validator)
       ;; count the number of matches
       count))

(defn parse-input [inp]
  (map (comp vec seq) (str/split-lines inp)))

(defn count-matches [inp search-offsets search-validator]
  (let [grid (parse-input inp)
        max-y (count grid)
        max-x (count (first grid))
        all-coordinates (for [x (range max-x)
                              y (range max-y)]
                          [x y])]
    (->> all-coordinates
         (map
          ;; use partial so that the function
          ;; only takes x-y coordinates
          (partial count-matches-around
                   grid
                   (partial cu/grid-in-range? max-x max-y)
                   search-offsets
                   search-validator))
         cu/sum)))

(def part-1-search [\X \M \A \S])

(defn part1 [inp]
  (let [offsets
        ;; generate the offset for each 4 letter search in
        ;; each of the compass directions
        (cu/grid-transpose
         (for [i (range 4)]
           [[0 i] [i i] [i 0] [i (- i)]
            [0 (- i)] [(- i) (- i)] [(- i) 0] [(- i) i]]))
        checker #(cu/vec-eq? % part-1-search)]
    (count-matches inp offsets checker)))

(def part-2-search [\M \A \S])

(defn part2 [inp]
  (let [offsets
        [[;; top left to bottom right
          [-1 1] [0 0] [1 -1]
          ;; top right to bottom left
          [1 1] [0 0] [-1 -1]]]
        valid-mas? #(or (cu/vec-eq? % part-2-search)
                        (cu/vec-eq? (reverse %) part-2-search))
        checker (fn [search]
                  (let [[m0 m1] (split-at 3 search)]
                    (and
                     (valid-mas? m0)
                     (valid-mas? m1))))]
    (count-matches inp offsets checker)))

