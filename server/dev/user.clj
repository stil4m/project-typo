(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [project-typo.system :refer [new-system]]))

(reloaded.repl/set-init! #(new-system {:port 5333
                                       :database {:host "localhost"
                                                  :port 28015
                                                  :token 0
                                                  :auth-key ""
                                                  :db "typpo"}}))
