(ns advent-of-code-2024.day6
  (:require
   [instaparse.core :as insta]
   [advent-of-code-2024.common-utils :as cu]))

;; [0,0] is top left, we only have positive x and y
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

(defn get-starting-location
  ;; Get the x-y coordinates of the starting location
  [grid]
  (first
   (for [[y row] (map-indexed vector grid)
         [x val] (map-indexed vector row)
         :when (= :start val)]
     [x y])))

(defn move
  "Give the next position after having moved, or nil if the move results
  in going outside of the grid"
  [pos direction grid obstacle-place-pos]
  (let [new-pos (get-move-pos pos direction)]
    (if (not (cu/grid-in-range? grid new-pos))
      nil
      (let [at-position (cu/grid-get grid new-pos)]
        (cond
          (or
           (= new-pos obstacle-place-pos)
           (= at-position :obstacle))
          {:pos pos :dir (get direction-rotate direction)}

          :else
          {:pos new-pos :dir direction})))))

(defn patrol
  "Get the coordinates and directions for a patrol that moves until
  they're outside of the grid."
  ([grid {:keys [pos dir]} obstacle-place-pos]
   (loop [pos pos
          direction dir
          seen #{}
          ordered-seen '()]
     (let [next (move pos direction grid obstacle-place-pos)
           {next-pos :pos next-dir :dir} next
           curr {:pos pos :dir direction}
           new-seen (conj seen curr)
           new-ordered-seen (cons curr ordered-seen)]
       (cond
         ;; out of bounds
         (nil? next-pos)
         (vec (reverse new-ordered-seen))

         (contains? seen next)
         :loop

         :else
         (recur next-pos next-dir new-seen new-ordered-seen)))))
  ([grid start]
   (patrol grid start nil))
  ([grid]
   (patrol grid {:pos (get-starting-location grid) :dir :north} nil)))

(defn part1 [inp]
  (->> inp
       parse-input
       patrol
       (map :pos)
       ;; dedup, since a position can appear multiple
       ;; times if it was encountered in different directions
       (into #{})
       count))

(defn is-valid-obstacle-pos? [grid obstacle-pos]
  (and
   (cu/grid-in-range? grid obstacle-pos)
   (= (cu/grid-get grid obstacle-pos) :empty)))

(defn part2 [inp]
  (let [grid (->> inp
                  parse-input)
        initial-path (patrol grid)]
    (->> initial-path
         ;; we try to put an obstacle right in front of each position
         ;; in the path
         (partition 2 1)
         (reduce (fn [{:keys [tried-obstacle-pos looping] :as acc} [start obstacle]]
                   (let [obstacle-pos (:pos obstacle)]
                     ;; we avoid placing an obstacle somewhere we've already tried
                     (if (or
                          (not (is-valid-obstacle-pos? grid obstacle-pos))
                          (contains? tried-obstacle-pos obstacle-pos))
                       acc
                       (let [result (patrol grid start obstacle-pos)]
                         {:tried-obstacle-pos (conj tried-obstacle-pos obstacle-pos)
                          :looping (if (= result :loop)
                                     (cons obstacle-pos looping)
                                     looping)}))))
                 {:tried-obstacle-pos #{} :looping '()})
         :looping
         (into #{})
         count)))

