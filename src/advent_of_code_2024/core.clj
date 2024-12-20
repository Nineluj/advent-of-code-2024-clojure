(ns advent-of-code-2024.core
  (:require
   [advent-of-code-2024.common-utils :as cu]
   [clojure.tools.cli :refer [parse-opts]]
   [criterium.core :as cc]
   [advent-of-code-2024.day1 :as day1]
   [advent-of-code-2024.day2 :as day2]
   [advent-of-code-2024.day3 :as day3]
   [advent-of-code-2024.day4 :as day4]
   [advent-of-code-2024.day5 :as day5]
   [advent-of-code-2024.day6 :as day6]
   [advent-of-code-2024.day7 :as day7]
   [advent-of-code-2024.day8 :as day8]))

(def cli-options
  ;; An option with a required argument
  [["-d" "--day DAY" "Problem day"
    :parse-fn #(Integer/parseInt %)
    :validate [#(<= 1 % 25) "Must be a number between 1 and 25"]]
   ["-p" "--part PART" "Problem part"
    :parse-fn #(Integer/parseInt %)
    :default 1
    :validate [#(<= 1 % 2) "Must be a number between 1 and 2"]]
   ["-t" "--test" "Use testing data?"]])

;; Define handlers using vars (#') instead of direct function references
;; to allow for dynamic function redefinition during REPL development.
;; When functions are redefined, the handlers map will automatically
;; use the new implementations.
(def handlers
  {1 {1 #'day1/part1
      2 #'day1/part2}
   2 {1 #'day2/part1
      2 #'day2/part2}
   3 {1 #'day3/part1
      2 #'day3/part2}
   4 {1 #'day4/part1
      2 #'day4/part2}
   5 {1 #'day5/part1
      2 #'day5/part2}
   6 {1 #'day6/part1
      2 #'day6/part2}
   7 {1 #'day7/part1
      2 #'day7/part2}
   8 {1 #'day8/part1
      2 #'day8/part2}})

(defn run [day part test?]
  (let [input (cu/get-input day test?)
        handler (-> handlers
                    (get day)
                    (get part))]
    (handler input)))

(defn bench []
  (doseq [[day value] handlers]
    (println "=> Day" day)
    (doseq [[part handler] value]
      (println ">>> Part" part)
      (let [input (cu/get-input day false)]
        (cc/quick-bench
         (handler input))))))

(defn -main
  "Used to dispatch tasks from the command line.
  Usage:
    lein run <task-number>         ; runs on default port (6337)
    lein run <task-number> <port>  ; runs on specified port"
  [& args]
  (let [parsed-opts (parse-opts args cli-options)
        opts (:options parsed-opts)]
    (run (:day opts) (:part opts) (:test? opts))))
