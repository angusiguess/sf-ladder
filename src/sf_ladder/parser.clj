(ns sf-ladder.parser)

;; Readers

(defmulti readf (fn [env k params] k))

(defmethod readf :default
  [_ k _]
  {:value {:error (str "No handler for read key" k)}})

(defmethod readf :competitors/by-email [env k params]
  (let [{:keys [conn query]} env
        {:keys [email]} params]))


;; Mutators
