(ns ui.connection.actions
  (:require [schema.core :as s :include-macros true]
            [ui.schema.base :refer [UserIdentifier ChannelIdentifier Status]]))


(defn data->action
  [action d]
  {:action action
   :data d})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Autenticate
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema Authenticate
  {:username s/Str
   :full-name s/Str})

(s/defn authenticate
  [d :- Authenticate]
  (data->action :authenticate d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Logout
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defn logout
  []
  (data->action :logout {}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Set Status
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema SetStatus
  {:status Status})

(s/defn set-status
  [d :- SetStatus]
  (data->action :set-status d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; List People
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defn list-people
  []
  (data->action :list-people {}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; List Channels
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defn list-channels
  []
  (data->action :list-channels {}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Get Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema GetChannel
  {:channel ChannelIdentifier})

(s/defn get-channel
  [d :- GetChannel]
  (data->action :get-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Create Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema CreateChannel
  {:name s/Str
   :room s/Bool
   :members [UserIdentifier]})

(s/defn create-channel
  [d :- CreateChannel]
  (data->action :create-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Create Conversation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema CreateConversation
  {:user UserIdentifier})

(s/defn create-conversation
  [d :- CreateConversation]
  (data->action :create-conversation d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Join Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema JoinChannel
  {:channel ChannelIdentifier})

(s/defn join-channel
  [d :- JoinChannel]
  (data->action :join-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invite User
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema InviteUser
  {:channel ChannelIdentifier
   :user UserIdentifier
   :admin s/Bool})

(s/defn invite-user
  [d :- InviteUser]
  (data->action :invite-user d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Change Channel Name
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema ChangeChannelName
  {:channel ChannelIdentifier
   :name UserIdentifier})

(s/defn change-channel-name
  [d :- ChangeChannelName]
  (data->action :change-channel-name d))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Join Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema LeaveChannel
  {:channel ChannelIdentifier})

(s/defn leave-channel
  [d :- LeaveChannel]
  (data->action :leave-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Send Message
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema SendMessage
  {:channel ChannelIdentifier
   :client-id s/Str
   :body s/Str})

(s/defn send-message
  [d :- SendMessage]
  (data->action :send-message d))