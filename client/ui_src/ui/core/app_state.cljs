(ns ui.core.app-state)

(def default-app-state
  {:route {:history []
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
   :current-room "room3"
   :open-rooms #{"room1"
                 "room3"}
   :message-queue []
   :rooms {"room1" {:id "room1"
                    :name "Just Jokes"
                    :description "Wait whut?!"
                    :private false
                    :people ["matstijl"
                             "tomtheun"
                             "maaarts"
                             "guest1234"]
                    :messages [{:user "guest1234"
                                :time 1450041030000
                                :message "Weet je hoe ze een stalker noemen met ski-stokken?"}
                               {:user "mitkuijp"
                                :time 1450041040000
                                :message "Wat dan flip?"}
                               {:user "guest1234"
                                :time 1450041050000
                                :message "Een Nordic Stalke."}
                               {:user "tomtheun"
                                :time 1450041060000
                                :message "Classic :p"}]}
           "room2" {:id "room2"
                    :name "TPL"
                    :description "TPL people room. Own people first"
                    :private true
                    :people ["matstijl"
                             "tomtheun"
                             "maaarts"]
                    :messages [{:user "matstijl"
                                :time 1450041030000
                                :message "Anyone there!?"}]}
           "room3" {:id "room3"
                    :name "Christmas Hackton"
                    :description nil
                    :private true
                    :people ["matstijl"
                             "mitkuijp"
                             "tomtheun"
                             "maaarts"]
                    :messages [{:user "tomtheun"
                                :message "Hi"
                                :time 1450041030000}
                               {:user "mitkuijp"
                                :message "Morning, had a good sleep?"
                                :time 1450041040000}
                               {:user "tomtheun"
                                :message "Do you know where @stil4m is?"
                                :time 1450041050000}
                               {:user "stil4m"
                                :message "Here I am"
                                :time 1450041060000}
                               {:user "stil4m"
                                :message "Sorry I am late"
                                :time 1450041070000}
                               {:user "mitkuijp"
                                :message "Always the same song :p"
                                :time 1450041080000}
                               {:user "tomtheun"
                                :message "Ok guys, lets get to it. The plan for today is to build an awesome chat client. I hope you have brought your pyjamas, because it is going to be a long weekend."
                                :time 1450041090000}
                               {:user "stil4m"
                                :message "Got them with me"
                                :time 1450041100000}
                               {:user "mitkuijp"
                                :message "Yes"
                                :time 1450041110000}
                               {:user "tomtheun"
                                :message "Who wants (beer)?"
                                :time 1450041120000}
                               {:user "stil4m"
                                :message "(areyoukiddingme)"
                                :time 1450041130000}
                               {:user "mitkuijp"
                                :message "Yep"
                                :time 1450041140000}
                               {:user "mitkujp"
                                :message "s/Yep/Yes"
                                :time 1450041150000}
                               {:user "stil4m"
                                :message "Ok, I'll will also have one, but only one."
                                :time 1450041160000}
                               {:user "tomtheun"
                                :message "3 beers coming up"
                                :time 1450041170000}]}}})
