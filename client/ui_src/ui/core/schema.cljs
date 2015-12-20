(ns ui.core.schema
  (:require [schema.core :as s]))

(def ValidBody
  #"[^\\s]")

(def UserIdentifier s/Str)

(def PersistedMessage
  "A schema for the message that was broadcased by the server"
  {:channel s/Str
   :client-id s/Str
   :id s/Str
   :time js/Date
   :user UserIdentifier
   :body ValidBody})

(def QueuedMessage
  "A schema for the message that was was send "
  {:body ValidBody
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
  {:id s/Str
   :name s/Str
   :room s/Bool
   :unread s/Int
   :messages [PersistedMessage]
   :members [UserIdentifier]
   (s/optional-key :current-message) (s/maybe s/Str)
   :queue [QueuedMessage]})

(def Channels
  {s/Str Channel})

(def Status
  (s/enum :available :offline :busy))

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
   :subscribed-channels [s/Str]
   :current-channel (s/maybe s/Str)
   :new-channel-page NewChannelPage
   :channels Channels})
