(ns ui.new-channels.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]
            [clojure.string :as str]))

(defn match-by-prop
  [prop query]
  (fn [item]
    (when-let [v (get item prop)]
      (re-matches (re-pattern
                   (str ".*" (str/lower-case query) ".*"))
                  (str/lower-case v)))))

(defn sort-and-filter-by-prop
  [list prop query]
  (sort
   #(compare (str/lower-case (get %1 prop)) (str/lower-case (get %2 prop)))
   (filterv
    (match-by-prop prop query)
    list)))

(register-sub
 :new-channel-page
 (fn [db]
   (reaction (:new-channel-page @db))))

(register-sub
 :new-channel-filter-query
 (fn [db]
   (let [new-channel-page (subscribe [:new-channel-page])]
     (reaction (:query @new-channel-page)))))

(defn split-by-bool-prop
  [list prop true-key false-key]
  (reduce
   (fn [m i]
     (update-in m [(if (get i prop) true-key false-key)] conj i))
   {}
   list))

(register-sub
 :available-channels
 (fn [db]
   (let [current-user (subscribe [:current-user])
         query (subscribe [:new-channel-filter-query])
         people (subscribe [:people/other-people])
         vs (vec (vals (:channels @db)))
         grouped (split-by-bool-prop vs
                                     :room
                                     :rooms
                                     :people)
         conversation-map (into {}
                                (map
                                 (fn [channel] [(first (filter #(not= (:username @current-user) %) (:members channel))) channel])
                                 (:people grouped)))]
     (reaction {:people (vec
                         (map
                          #(assoc % :channel (get conversation-map (:username %)))
                          (sort-and-filter-by-prop @people :full-name @query)))
                :rooms (vec (sort-and-filter-by-prop (:rooms grouped) :name @query))}))))
