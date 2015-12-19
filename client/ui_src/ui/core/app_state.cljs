(ns ui.core.app-state)

(def default-app-state
  {:connection {:address "ws://10.53.6.129:5333"
                :ws nil}
   :route {:history []
           :active nil
           :future []}
   :authenticating false
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
   :current-channel nil
   :open-channels []
   :channels {}})
