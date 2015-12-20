(ns ui.schema.app-state
  (:require [schema.core :as s]
            [ui.schema.base :refer [UserIdentifier ChannelIdentifier Status]]))

(def ValidBody
  #"[^\s]")

(def PersistedMessage
  "A schema for the message that was broadcased by the server"
  {:channel ChannelIdentifier
   :client-id s/Str
   :id s/Str
   :time s/Num
   :user UserIdentifier
   :body ValidBody})

(def QueuedMessage
  "A schema for the message that was was send "
  {:channel ChannelIdentifier
   :body ValidBody
   :user UserIdentifier
   :client-id s/Str})

(def ServerConnection
  {:ws (s/maybe s/Any)
   :address (s/maybe s/Str)})

(def LoginForm
  {:authenticating s/Bool
   :loading s/Bool
   :error (s/maybe s/Any)
   :data {:username s/Str
          :full-name s/Str}})

(def Route
  {:active s/Any
   :history [s/Any]
   :future [s/Any]})

(def Channel
  {:id ChannelIdentifier
   :name s/Str
   :room s/Bool
   :unread s/Int
   :messages [PersistedMessage]
   :members [UserIdentifier]
   (s/optional-key :current-message) (s/maybe s/Str)
   :queue [QueuedMessage]})

(def Channels
  {ChannelIdentifier Channel})

(def CurrentUser
  {:username s/Str
   :full-name s/Str
   :status Status})

(def Person
  {:id s/Str
   :full-name s/Str
   :status Status})

(def NewChannelPage
  {:query s/Str})

(def AppState
  {:connection ServerConnection
   :login-form LoginForm
   :user (s/maybe CurrentUser)
   :people [Person]
   :route Route
   :subscribed-channels [ChannelIdentifier]
   :current-channel (s/maybe ChannelIdentifier)
   :new-channel-page NewChannelPage
   :channels Channels})
