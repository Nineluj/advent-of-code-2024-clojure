(ns advent-of-code-2024.day6
  (:require
   [instaparse.core :as insta]
   [advent-of-code-2024.common-utils :as cu]))

(def parser
  (insta/parser
   "map = (line <'\n'>)+ line
    line = (obstacle|start|empty)*
    obstacle = '#'
    start = '^'
    empty = '.'"))

(defn parse-input [input]
  (let [parsed (parser input)]
    (if (insta/failure? parsed)
      (throw (ex-info "Parsing failed"
                      {:error (insta/get-failure parsed)}))
      (->> parsed
           (insta/transform
            {:obstacle (fn [_] :obstacle)
             :start (fn [_] :start)
             :empty (fn [_] :empty)
             :line vector
             :map vector})))))

(defn get-starting-location
  ;; Get the x-y coordinates of the starting location
  [grid]
  (first
   (for [[y row] (map-indexed vector grid)
         [x val] (map-indexed vector row)
         :when (= :start val)]
     [x y])))

(def direction-rotate
  {:north :east
   :east :south
   :south :west
   :west :north})

(def direction-offset
  {:north [0 -1]
   :east [1 0]
   :south [0 1]
   :west [-1 0]})

(defn get-move-pos [[x y] direction]
  (let [[offset-x offset-y] (get direction-offset direction)]
    [(+ x offset-x) (+ y offset-y)]))

(defn move
  "Give the next position after having moved, or nil if the move results
  in going outside of the grid"
  [pos direction grid obstacle-place-pos]
  (let [max-y (count grid)
        max-x (count (first grid))
        new-pos (get-move-pos pos direction)]
    (if (not (cu/grid-in-range? max-x max-y new-pos))
      nil
      (let [at-position (cu/grid-get grid new-pos)]
        (cond
          (or
           (= at-position obstacle-place-pos)
           (= at-position :obstacle))
          [pos (get direction-rotate direction)]

          :else
          [new-pos direction])))))

(defn patrol
  "Get the coordinates and directions for a patrol that moves until
  they're outside of the grid."
  ([grid {:keys [pos dir]} obstacle-place-pos]
   (loop [pos pos
          direction dir
          acc '()
          seen #{}]
     (let [[next-pos next-dir :as next] (move pos direction grid obstacle-place-pos)
           curr {:pos pos :dir direction}
           new-acc (cons curr acc)
           new-seen (conj curr seen)]
       (cond
         ;; out of bounds
         (nil? next-pos)
         (vec (reverse new-acc))

         (contains? seen next)
         :loop

         :else
         (recur next-pos next-dir new-acc new-seen)))))
  ([grid start]
   (patrol grid start nil))
  ([grid]
   (patrol grid {:pos (get-starting-location grid) :dir :north} nil)))

;; [0,0] is top left, we only have positive x and y

(defn part1 [inp]
  (->> inp
       parse-input
       patrol
       (into #{} (map :pos))
       count))

(defn placing-obstacle-causes-loop? [grid {:keys [pos dir] :as start}]
  (let [obstacle-pos (get-move-pos pos dir)
        result (patrol grid start obstacle-pos)]
    (println result)
    (if (= :loop result)
      obstacle-pos
      nil)))

(defn part2 [inp]
  (let [grid (->> inp
                  parse-input)
        initial-path (patrol grid)]
    (->> initial-path
         (map #(placing-obstacle-causes-loop? grid %)))))

(def sample (cu/get-input 6 true))

;; (def sample-2 ".#................
;; ..#...............
;; ..................
;; .^................
;; ..................")

;; (part1 sample)
;; (part2 sample)
;; (part2 sample-2)
