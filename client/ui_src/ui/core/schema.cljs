(ns ui.core.schema
  (:require [schema.core :as s]))

; Message
; Queued Message
; Create Message

;Login Form
; Channel

(def ValidBody
  #"[^\\s]")

(def PersistedMessage
  "A schema for the message that was broadcased by the server"
  {(s/required-key :channel) s/Str
   (s/required-key :client-id) s/Str
   (s/required-key :id) s/Str
   (s/required-key :time) js/Date
   (s/required-key :user) s/Str
   (s/required-key :body) ValidBody})


(def QueuedMessage
  "A schema for the message that was was send "
  {(s/required-key :body) ValidBody
   (s/required-key :client-id) s/Str})

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
   :unread s/Int
   :messages [PersistedMessage]
   (s/optional-key :current-message) (s/maybe s/Str)
   :queue [QueuedMessage]})

(def Channels
  {s/Str Channel})

(def Status
  (s/enum :online :offline :busy))

(def CurrentUser
  {:username s/Str
   :full-name s/Str
   :status Status})

(def Person
  {:id s/Str
   :full-name s/Str
   :status Status})

(def AppState
  {(s/required-key :connection) ServerConnection
   (s/required-key :login-form) LoginForm
   (s/required-key :user) (s/maybe CurrentUser)
   (s/required-key :people) [Person]
   (s/required-key :route) Route
   (s/required-key :subscribed-channels) [s/Str]
   :current-channel (s/maybe s/Str)
   (s/required-key :channels) Channels})