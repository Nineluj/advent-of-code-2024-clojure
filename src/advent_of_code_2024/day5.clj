;; sample file to copy
(ns advent-of-code-2024.day5
  (:require [clojure.edn]
            [clojure.string :as str]
            [advent-of-code-2024.common-utils :as cu]))

(defn split-around
  "Split around the first element matching the predicate,
  skipping that element."
  [pred xs]
  (let [split-index (first (keep-indexed #(when (pred %2) %1) xs))
        [pre post] (split-at split-index xs)]
    [pre (rest post)]))

(defn parse-ordering [lines]
  (map
   (fn [order-line]
     (let [[before after]
           (as-> order-line v
             (str/split v #"\|")
             (map clojure.edn/read-string v))]
       {:before before :after after}))
   lines))

(defn parse-updates [lines]
  (map
   (fn [update-line]
     (as-> update-line v
       (str/split v #",")
       (map clojure.edn/read-string v)))
   lines))

(defn parse-input [inp]
  (let [lines (str/split-lines inp)
        [ordering-lines updates-lines] (split-around str/blank? lines)]
    {:ordering (parse-ordering ordering-lines)
     :updates (parse-updates updates-lines)}))

(defn get-middle-element [xs]
  (let [col (vec xs)
        len (count col)
        mid-index (quot len 2)]
    (get col mid-index)))

(defn get-order-dependencies [ordering]
  (as-> ordering v
    (group-by :before v)
    (update-vals v (fn [values]
                     (->> values
                          (map :after)
                          set)))))

(defn check [dependencies update]
  (loop [xs update
         prev '()]
    (if-let [x (first xs)]
      (and
       (not (some #(contains? (get dependencies x) %) prev))
       (recur (rest xs) (cons x prev)))
      true)))

(defn fix-ordering [dependencies update]
  (sort
   (fn [x0 x1]
     (let [x0-deps (get dependencies x0)
           x1-deps (get dependencies x1)]
       (cond (contains? x0-deps x1) 1
             (contains? x1-deps x0) -1
             :else 0)))
   update))

(defn part1 [inp]
  (let [{:keys [ordering updates]} (parse-input inp)
        dependencies (get-order-dependencies ordering)]
    (->> updates
         (filter (partial check dependencies))
         (map get-middle-element)
         cu/sum)))

(defn part2 [inp]
  (let [{:keys [ordering updates]} (parse-input inp)
        dependencies (get-order-dependencies ordering)]
    (->> updates
         (filter (comp not (partial check dependencies)))
         (map (partial fix-ordering dependencies))
         (map get-middle-element)
         cu/sum)))
