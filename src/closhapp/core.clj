(ns closhapp.core
  (:require [closhapp.app :as app]
            [user]
            [clojure.string :as str]
            [dk.salza.liq.core :as core]
            [dk.salza.liq.editor :as editor]))

(defn inline-eval
  []
  (editor/prompt-set (:stdout (user/closh (editor/get-line))))
  (Thread/sleep 10)
  (editor/request-fullupdate))

(defn set-scratch
  []
  (editor/clear)
  (editor/insert (str/join "\n" [
    "# Inline evaluation"
    "Navigate to a line with a command and press F5 to evaluate using closh"
    "It is not perfect, so if screen gets messed up type C-g to force refresh!"
    ""
    "ls"
    "pwd"
    "ls \"/tmp\""
    ""
    "Mix text and commands"
    "cd \"/tmp\""
    "echo \"hi\" | (clojure.string/upper-case)"
    "(list :a :b :c) |> (count)"
    ""
    "# Some kind of shell"
    "Press F6 to start a shell."
    "Typing enter will evaluate lines more shell like."
    "Just start by typing \"ls\" and enter to see how it works."])))



(defn -main
  [& args]
  (core/set-defaults)
  (apply core/startup args)
  (core/init-editor)
  (set-scratch)
  (editor/beginning-of-buffer)
  (editor/set-global-key "f5" inline-eval)
  (editor/set-global-key "f6" app/run))

