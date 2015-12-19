(ns ui.core.app-state
  (:require [cljs-uuid-utils.core :as uuid]))

(def channel-1-id (uuid/make-random-uuid))
(def channel-2-id (uuid/make-random-uuid))
(def channel-3-id (uuid/make-random-uuid))

(defn to-actual-messages
  [start list]
  (vec (map-indexed
        (fn [n {:keys [user body]}]
          {:client-id (uuid/make-random-uuid)
           :server-id (uuid/make-random-uuid)
           :user user
           :body body
           :time (+ start (* n 20000))})
        list)))

(def default-app-state
  {:connection {:address "ws://10.31.1.21:5333"
                :ws nil}
   :route {:history []
           :active nil
           :future []}
   :user {:username "stil4m"
          :full-name "Mats Stijlaart"
          :description ""}
   :login-form {:loading false
                :error nil
                :data {:username "matstijl"
                       :full-name "Mats Stijlaart"}}
   :people [{:id "matstijl" :full-name "Mats Stijlaart" :status :online}
            {:id "mitkuijp" :full-name "Tomas Theunissen" :status :online}
            {:id "tomtheun" :full-name "Mitchel Kuijpers" :status :busy}
            {:id "maaarts" :full-name "Maarten Arts" :status :online}
            {:id "guest1234" :full-name "Philip Geubels" :status :online}]
   :current-channel channel-3-id
   :open-channels [channel-1-id
                   channel-2-id
                   channel-3-id]
   :channels (assoc {}
               channel-1-id {:id channel-1-id
                             :name "Just Jokes"
                             :description "Wait whut?!"
                             :private false
                             :unread 3
                             :people ["matstijl"
                                      "tomtheun"
                                      "maaarts"
                                      "guest1234"]
                             :queue []
                             :messages (to-actual-messages
                                        1450041030000
                                        [{:user "guest1234"
                                          :body "Weet je hoe ze een stalker noemen met ski-stokken?"}
                                         {:user "mitkuijp"
                                          :body "Wat dan flip?"}
                                         {:user "guest1234"
                                          :body "Een Nordic Stalker."}
                                         {:user "tomtheun"
                                          :body "Classic :p"}])}
               channel-2-id {:id channel-2-id
                             :name "TPL"
                             :description "TPL people channel. Own people first"
                             :private true
                             :people ["matstijl"
                                      "tomtheun"
                                      "maaarts"]
                             :messages (to-actual-messages
                                        1450041030000
                                        [{:user "matstijl"
                                          :body "Anyone there!?"}])
                             :queue [{:client-id (uuid/make-random-uuid)
                                      :server-id (uuid/make-random-uuid)
                                      :user "matstijl"
                                      :time nil
                                      :body "I guess this message takes a long time to send..."}]}
               channel-3-id {:id channel-3-id
                             :name "Christmas Hackton"
                             :description nil
                             :private true
                             :people ["matstijl"
                                      "mitkuijp"
                                      "tomtheun"
                                      "maaarts"]
                             :queue []
                             :messages (to-actual-messages
                                        1450041030000
                                        [{:user "tomtheun"
                                          :body "Hi"}
                                         {:user "mitkuijp"
                                          :body "Morning, had a good sleep?"}
                                         {:user "tomtheun"
                                          :body "Do you know where @stil4m is?"}
                                         {:user "stil4m"
                                          :body "Here I am"}
                                         {:user "stil4m"
                                          :body "Sorry I am late"}
                                         {:user "mitkuijp"
                                          :body "Always the same song :p"}
                                         {:user "tomtheun"
                                          :body "Ok guys, lets get to it. The plan for today is to build an awesome chat client. I hope you have brought your pyjamas, because it is going to be a long weekend."}
                                         {:user "stil4m"
                                          :body "Got them with me"}
                                         {:user "mitkuijp"
                                          :body "Yes"}
                                         {:user "tomtheun"
                                          :body "Who wants (beer)?"}
                                         {:user "stil4m"
                                          :body "(areyoukiddingme)"}
                                         {:user "mitkuijp"
                                          :body "Yep"}
                                         {:user "mitkujp"
                                          :body "s/Yep/Yes"}
                                         {:user "stil4m"
                                          :body "Ok, I'll will also have one, but only one."}
                                         {:user "tomtheun"
                                          :body "3 beers coming up"}])})})

