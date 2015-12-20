(ns ui.new-channels.handlers
  (:require [re-frame.core :refer [register-handler
                                   dispatch
                                   path
                                   after]]
            [ui.core.typo-re-frame :refer [default-middleware]]))

(register-handler
 :new-channel/change-filter
 [default-middleware (path [:new-channel-page :query])]
 (fn [_ [new-query]]
   new-query))

