(ns ui.core.app-state)

(def default-app-state
  {:connection {:address "ws://localhost:5333"
                :ws nil}
   :route {:history []
           :active nil
           :future []}
   :user nil
   :login-form {:authenticating false
                :loading false
                :error nil
                :data {:username ""
                       :full-name ""}}
   :new-channel-page {:query ""}
   :people []
   :current-channel nil
   :subscribed-channels []
   :message-handlers {}
   :channels {}})
