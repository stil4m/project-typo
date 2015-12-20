(ns ui.new-channels.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]
            [clojure.string :as str]))

(def people
  [{:username "zz"
    :full-name "ZZ top"
    :status :available}
   {:username "aa"
    :full-name "Albert Aronson"
    :status :available}
   {:username "bb"
    :full-name "Bert Boos"
    :status :busy}
   {:username "cc"
    :full-name "Claudia Cornelissen"
    :status :offline}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}
   {:username "matstijl"
    :full-name "Mats Stijlaart"
    :status :available}])

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
   (let [query (subscribe [:new-channel-filter-query])
         vs (vec (vals (:channels @db)))
         grouped (split-by-bool-prop (concat vs people)     ;TODO
                                :room
                                :rooms
                                :people)]
     (reaction {:people (vec (sort-and-filter-by-prop (:people grouped) :full-name @query))
                :rooms (vec (sort-and-filter-by-prop (:rooms grouped) :name @query))}))))
