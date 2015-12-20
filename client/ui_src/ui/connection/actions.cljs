(ns ui.connection.actions
  (:require [schema.core :as s :include-macros true]))


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
;; List Channels
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defn list-channels
  []
  (data->action :list-channels {}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Get Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema GetChannel
  {:channel s/Str})

(s/defn get-channel
  [d :- GetChannel]
  (data->action :get-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Create Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema CreateChannel
  {:name s/Str})

(s/defn create-channel
  [d :- CreateChannel]
  (data->action :create-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Create Conversation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema CreateConversation
  {:user s/Str})

(s/defn create-conversation
  [d :- CreateConversation]
  (data->action :create-conversation d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Join Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema JoinChannel
  {:channel s/Str})

(s/defn join-channel
  [d :- JoinChannel]
  (data->action :join-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Invite User
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema InviteUser
  {:channel s/Str
   :user s/Str
   :admin s/Bool})

(s/defn invite-user
  [d :- InviteUser]
  (data->action :invite-user d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Change Channel Name
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema ChangeChannelName
  {:channel s/Str
   :name s/Str})

(s/defn change-channel-name
  [d :- ChangeChannelName]
  (data->action :change-channel-name d))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Join Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema LeaveChannel
  {:channel s/Str})

(s/defn leave-channel
  [d :- LeaveChannel]
  (data->action :leave-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Send Message
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema SendMessage
  {:channel s/Str
   :client-id s/Str
   :body s/Str})

(s/defn send-message
  [d :- SendMessage]
  (data->action :send-message d))