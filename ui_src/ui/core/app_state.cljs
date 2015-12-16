(ns ui.core.app-state)

(def default-app-state
  {:route {:history []
           :active nil
           :future []}
   :user {:username "stil4m"
          :full-name "Mats Stijlaart"
          :description ""}
   :conversation {:people [{:id "tomtheun"
                            :full-name "Tomas Theunissen"}
                           {:id "mitkuijp"
                            :full-name "Mitchel Kuijpers"}]
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
                              :time 1450041170000}]}})
