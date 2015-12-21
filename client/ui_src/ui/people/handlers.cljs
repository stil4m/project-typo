(ns ui.people.handlers
  (:require [re-frame.core :refer [register-handler path]]
            [ui.core.typo-re-frame :refer [default-middleware]]
            [ui.people.transitions :as transitions]))

(register-handler
 :fetched-all-people
 [default-middleware (path [:people])]
 transitions/set-people)

