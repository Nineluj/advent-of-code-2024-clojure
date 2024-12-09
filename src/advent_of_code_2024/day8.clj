(ns advent-of-code-2024.day8
  (:require [clojure.edn]
            [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [advent-of-code-2024.common-utils :as cu]))

(def EMPTY_CELL \.)

(defn parse-input [input]
  (->> input
       str/split-lines
       (map #(vec (seq %)))
       (into [])))

(defn get-antena-locations [grid]
  (for [[rowi row] (map-indexed vector grid)
        [coli x] (map-indexed vector row)
        :when (not= EMPTY_CELL x)]
    {:antena x :pos [coli rowi]}))

(defn group-by-antena-type [antena-locations]
  (->> antena-locations
       (group-by :antena)
       (map (fn [[k v]]
              [k (map :pos v)]))))

(defn get-point-sequences
  [a1 a0 in-range?]
  (let [delta (cu/vec2d-subtract a1 a0)
        gen-forward #(cu/vec2d-add % delta)
        gen-backward #(cu/vec2d-subtract delta %)
        forward-seq (take-while in-range? (iterate gen-forward a0))
        backward-seq (take-while in-range? (iterate gen-backward a1))]
    {:forward forward-seq :backward backward-seq}))

(defn get-antinodes [locations in-range? repeat]
  (->> locations
       (#(combo/combinations % 2))
       (mapcat (fn [[a0 a1]]
                 (let [{fwd :forward bwd :backward} (get-point-sequences a0 a1 in-range?)]
                   (if repeat
                     (concat fwd bwd)
                     ;; with repeat for part 2 we include the antena locations,
                     ;; otherwise we skip them
                     (concat
                      (take 1 (rest fwd))
                      (take 1 (rest bwd)))))))))

(defn solve [inp repeat?]
  (let [grid (parse-input inp)
        max-y (count grid)
        max-x (count (first grid))
        in-range? #(cu/grid-in-range? max-x max-y %)]
    (->> grid
         get-antena-locations
         group-by-antena-type
         (mapcat (fn [[_ locations]]
                   (get-antinodes locations in-range? repeat?)))
         (into #{})
         count)))

(defn part1 [inp]
  (solve inp false))

(defn part2 [inp]
  (solve inp true))
