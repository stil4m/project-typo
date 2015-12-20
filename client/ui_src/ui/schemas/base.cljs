(ns ui.schema.base
  (:require [schema.core :as s]))

(def UserIdentifier s/Str)
(def ChannelIdentifier s/Str)
(def Status (s/enum :available :offline :busy))