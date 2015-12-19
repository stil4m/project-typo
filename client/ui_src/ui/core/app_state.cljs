(ns ui.core.app-state)

(def default-app-state
  {:connection {:address "ws://localhost:5333"
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
   :current-channel "channel3"
   :open-channels ["channel1"
                   "channel3"]
   :message-queue []
   :channels {"channel1" {:id "channel1"
                          :name "Just Jokes"
                          :description "Wait whut?!"
                          :private false
                          :unread 3
                          :people ["matstijl"
                                   "tomtheun"
                                   "maaarts"
                                   "guest1234"]
                          :messages [{:id 11
                                      :user "guest1234"
                                      :time 1450041030000
                                      :body "Weet je hoe ze een stalker noemen met ski-stokken?"}
                                     {:id 12
                                      :user "mitkuijp"
                                      :time 1450041040000
                                      :body "Wat dan flip?"}
                                     {:id 13
                                      :user "guest1234"
                                      :time 1450041050000
                                      :body "Een Nordic Stalker."}
                                     {:id 14
                                      :user "tomtheun"
                                      :time 1450041060000
                                      :body "Classic :p"}]}
               "channel2" {:id "channel2"
                           :name "TPL"
                           :description "TPL people channel. Own people first"
                           :private true
                           :people ["matstijl"
                                    "tomtheun"
                                    "maaarts"]
                           :messages [{:id 21
                                       :user "matstijl"
                                       :time 1450041030000
                                       :body "Anyone there!?"}]}
               "channel3" {:id "channel3"
                           :name "Christmas Hackton"
                           :description nil
                           :private true
                           :people ["matstijl"
                                    "mitkuijp"
                                    "tomtheun"
                                    "maaarts"]
                           :messages [{:id 31
                                       :user "tomtheun"
                                       :body "Hi"
                                       :time 1450041030000}
                                      {:id 32
                                       :user "mitkuijp"
                                       :body "Morning, had a good sleep?"
                                       :time 1450041040000}
                                      {:id 33
                                       :user "tomtheun"
                                       :body "Do you know where @stil4m is?"
                                       :time 1450041050000}
                                      {:id 34
                                       :user "stil4m"
                                       :body "Here I am"
                                       :time 1450041060000}
                                      {:id 35
                                       :user "stil4m"
                                       :body "Sorry I am late"
                                       :time 1450041070000}
                                      {:id 36
                                       :user "mitkuijp"
                                       :body "Always the same song :p"
                                       :time 1450041080000}
                                      {:id 37
                                       :user "tomtheun"
                                       :body "Ok guys, lets get to it. The plan for today is to build an awesome chat client. I hope you have brought your pyjamas, because it is going to be a long weekend."
                                       :time 1450041090000}
                                      {:id 38
                                       :user "stil4m"
                                       :body "Got them with me"
                                       :time 1450041100000}
                                      {:id 39
                                       :user "mitkuijp"
                                       :body "Yes"
                                       :time 1450041110000}
                                      {:id 310
                                       :user "tomtheun"
                                       :body "Who wants (beer)?"
                                       :time 1450041120000}
                                      {:id 311
                                       :user "stil4m"
                                       :body "(areyoukiddingme)"
                                       :time 1450041130000}
                                      {:id 312
                                       :user "mitkuijp"
                                       :body "Yep"
                                       :time 1450041140000}
                                      {:id 313
                                       :user "mitkujp"
                                       :body "s/Yep/Yes"
                                       :time 1450041150000}
                                      {:id 314
                                       :user "stil4m"
                                       :body "Ok, I'll will also have one, but only one."
                                       :time 1450041160000}
                                      {:id 315
                                       :user "tomtheun"
                                       :body "3 beers coming up"
                                       :time 1450041170000}]}}})
