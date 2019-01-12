(ns user
  (:require [clojure.tools.reader.reader-types :refer [string-push-back-reader]]
            ;[closh.zero.platform.process]
            [closh.zero.reader]
            [closh.zero.compiler :as compiler]
            [closh.zero.parser :as parser]
            [closh.zero.core :refer [shx expand expand-partial expand-redirect]]
            [closh.zero.builtin]
            [closh.zero.builtin :refer [exit quit getenv setenv]]
            [closh.zero.pipeline :refer [process-output process-value wait-for-pipeline pipe pipe-multi pipe-map pipe-filter pipeline-condition]]
            [clojure.string :as str]
            [closh.zero.macros :refer [sh sh-str sh-code sh-ok sh-seq sh-lines sh-value defalias defabbr defcmd]]
            [closh.zero.util :refer [source-shell]]))

(def cd closh.zero.builtin/cd)

(defn closh
  [cmd]
  (try
    (let [code (compiler/compile-batch
                 (parser/parse (closh.zero.reader/read (string-push-back-reader cmd))))]
      (binding [*ns* (create-ns 'user)]
        (process-value (eval code))))
    (catch Exception e {:stdout (.getMessage e)})))
