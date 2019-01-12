(ns closhapp.app
  (:require [user]
            [dk.salza.liq.editor :as editor]
            [dk.salza.liq.slider :refer :all]
            [dk.salza.liq.apps.textapp :as textapp]
            [clojure.tools.reader.reader-types :refer [string-push-back-reader]]
            [closh.zero.reader :as reader]
            [closh.zero.compiler :as compiler]
            [closh.zero.env]
            [clojure.string :as str]))

(def keymap-insert (atom {}))
(def keymap-navigation (atom {}))

(defn eval-line
  []
  (let [line (editor/get-line)]
    (editor/insert
      (str "\n"
           (:stdout (user/closh line))
           "\n"
       ))
    (Thread/sleep 10)
    (editor/request-fullupdate)))

(reset! keymap-navigation
  (assoc @textapp/keymap-navigation
    "\t" #(editor/set-keymap @keymap-insert)
    "o" (fn [] (do (editor/insert-line) (editor/set-keymap @keymap-insert)))))

(reset! keymap-insert
  (assoc @textapp/keymap-insert
    "\t" #(editor/set-keymap @keymap-navigation)
    "\n" eval-line))

(defn run
  []
  (editor/new-buffer "closh")
  (editor/insert "C-w to exit the app\n")
  (editor/set-keymap @keymap-insert))



