(ns advent-of-code-2024.helpers
  (:require [clojure.string :as str]))

(def input-dir "inputs")
(def sample-dir "samples")

(defn get-input [day-number test-data?]
  (let [dir (if test-data? sample-dir input-dir)
        path (str dir "/day" day-number ".txt")]
    (str/trim-newline (slurp path))))
