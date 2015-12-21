(ns ui.people.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]))

(register-sub
 :current-user
 (fn [db]
   (reaction (:user @db))))

(register-sub
 :people/all
 (fn [db]
   (reaction (:people @db))))

(register-sub
 :people/other-people
 (fn [db]
   (let [current-user (subscribe [:current-user])
         all-people (subscribe [:people/all])]
     (reaction (filterv
                #(not= (:username %) (:username @current-user))
                @all-people)))))