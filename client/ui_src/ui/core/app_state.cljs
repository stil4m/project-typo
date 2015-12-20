(ns ui.core.app-state)

(def default-app-state
  {:connection {:address "ws://10.31.1.21:5333"
                :ws nil}
   :route {:history []
           :active nil
           :future []}
   :user nil
   :login-form {:authenticating false
                :loading false
                :error nil
                :data {:username "matstijl"
                       :full-name "Mats Stijlaart"}}
   :new-channel-page {:query ""}
   :people [{:id "matstijl" :full-name "Mats Stijlaart" :status :available}
            {:id "mitkuijp" :full-name "Tomas Theunissen" :status :available}
            {:id "tomtheun" :full-name "Mitchel Kuijpers" :status :busy}
            {:id "maaarts" :full-name "Maarten Arts" :status :available}
            {:id "guest1234" :full-name "Philip Geubels" :status :available}]
   :current-channel nil
   :subscribed-channels []
   :channels {}})
