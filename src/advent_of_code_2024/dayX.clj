;; sample file to copy
(ns advent-of-code-2024.dayX
  (:require [clojure.edn]
            [instaparse.core :as insta]))

(def number-pair-parser
  (insta/parser ""))

(defn parse-input [input]
  (let [parsed (number-pair-parser input)]
    (if (insta/failure? parsed)
      (throw (ex-info "Parsing failed"
                      {:error (insta/get-failure parsed)}))
      (->> parsed
           (insta/transform
            {:number clojure.edn/read-string
             :line vector
             :document vector})))))

(defn part1 [inp] nil)
(defn part2 [inp] nil)
