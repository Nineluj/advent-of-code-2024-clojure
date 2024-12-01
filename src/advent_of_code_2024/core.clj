(ns advent-of-code-2024.core
  (:require
   [clojure.string :as str]
   [clojure.tools.cli :refer [parse-opts]]
   [advent-of-code-2024.day1 :as day1]))

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

(def input-dir "inputs")
(def sample-dir "samples")

(defn get-input [day-number test-data?]
  (let [dir (if test-data? sample-dir input-dir)
        path (str dir "/day" day-number ".txt")]
    (str/trim-newline (slurp path))))

(def handlers
  {1 {1 day1/part1
      2 day1/part2}})

(defn run [day part test?]
  (let [input (get-input day test?)
        handler (-> handlers
                    (get day)
                    (get part))]
    (handler input)))

(defn -main
  "Used to dispatch tasks from the command line.
  Usage:
    lein run <task-number>         ; runs on default port (6337)
    lein run <task-number> <port>  ; runs on specified port"
  [& args]
  (let [parsed-opts (parse-opts args cli-options)
        opts (:options parsed-opts)]
    (run (:day opts) (:part opts) (:test? opts))))
