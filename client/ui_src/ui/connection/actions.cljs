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
;; Create Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema CreateChannel
  {:name s/Str})

(s/defn create-channel
  [d :- CreateChannel]
  (data->action :create-channel d))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Join Channel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/defschema JoinChannel
  {:channel s/Str})

(s/defn join-channel
  [d :- JoinChannel]
  (data->action :join-channel d))


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