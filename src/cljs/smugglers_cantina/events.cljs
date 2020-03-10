(ns smugglers-cantina.events
  (:require
   [cljs.reader :refer [read-string]]
   [ajax.core :as ajax] 
   [clojure.string :as s]
   [re-frame.core :refer [reg-event-db
                          reg-event-fx
                          reg-fx
                          reg-cofx
                          inject-cofx
                          dispatch
                          ->interceptor]]
   [day8.re-frame.http-fx]
   [smugglers-cantina.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ["amazon-cognito-auth-js" :refer (CognitoAuth)]
   ["aws-sdk" :as aws]))

(def local-save-character
  (->interceptor
   :id :local-save-character
   :after (fn [ctx]
            (let [character (get-in ctx [:effects :db :character])]
              (.setItem js/localStorage
                        "character"
                        (str character))
              ctx))))

(reg-cofx
 :username
 (fn [coeffects _]
   (let [v (.getItem js/localStorage "username")]
     (assoc coeffects
            :username
            v))))

(reg-cofx
 :character
 (fn [coeffects _]
   (let [character (read-string (.getItem js/localStorage "character"))]
     (assoc coeffects
            :character
            character))))

(reg-cofx
 :jwt-token
 (fn [coeffects _]
   (let [v (.getItem js/localStorage "jwt-token")]
     (assoc coeffects
            :jwt-token
            v))))

(reg-cofx
 :local-store
 (fn [coeffects local-store-key]
   (assoc coeffects
          :local-store
          (js->clj (.getItem js/localStorage local-store-key)))))

(reg-cofx
  :auth
  (fn [cofx _]
    (let [url js/window.location.href
          localhost? (s/starts-with? url "http://localhost:8280")
          auth-data {"ClientId" (if localhost?
                                  "og8mukp0uqi7vokrfbiks11fj"
                                  "g084lvq352uvn98r5h53isuh8")
                     "UserPoolId" "us-east-1_Xncit47rK"
                     "RedirectUriSignIn" (if localhost?
                                           "http://localhost:8280"
                                           "https://smugglers-cantina.com")
                     "RedirectUriSignOut" (if localhost?
                                           "http://localhost:8280"
                                           "https://smugglers-cantina.com")
                     "AppWebDomain" "auth.smugglers-cantina.com"
                     "TokenScopesArray" ["openid" "email"]}

          auth (CognitoAuth. (clj->js auth-data))]
      #_(.useCodeGrantFlow auth)
      (set! (. auth -userhandler)
            (set! (. auth -userhandler)
                  (clj->js {"onSuccess" (fn [result]
                                          (dispatch [::login-success auth result]))
                            "onFailure" (fn [result]
                                          (dispatch [::login-failure result]))})))
      (assoc cofx :auth auth)
      #_(.parseCognitoWebResponse auth js/window.location.href))))

(reg-cofx
 :load-auth
 (fn [cofx [_ a]]
   (prn "LOAD AUTH")
   (let [auth (:auth cofx)]
     #_(.getSession auth)
     (.parseCognitoWebResponse auth js/window.location.href)
     {})))

(reg-event-fx
 ::initialize-db
 [(inject-cofx :username)
  (inject-cofx :jwt-token)
  (inject-cofx :auth)
  (inject-cofx :character)]
 (fn-traced [cofx _]
            (prn "INIT DB" (:character cofx))
            (try
              (.parseCognitoWebResponse (:auth cofx) js/window.location.href)
              (catch js/Object e))
            (prn "COFX" cofx)
            {:db (assoc db/default-db
                        :username (:username cofx)
                        :jwt-token (:jwt-token cofx)
                        :character (:character cofx))}))

(reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(reg-event-db
 ::set-name
 [local-save-character]
 (fn-traced [db [_ character-name]]
            (assoc-in db [:character :name] character-name)))

(reg-event-db
 ::set-species
 [local-save-character]
 (fn-traced [db [_ species-key]]
            (assoc-in db [:character :species] species-key)))

(reg-event-db
 ::set-career
 [local-save-character]
 (fn-traced [db [_ career-key]]
            (update db
                    :character
                    (fn [character]
                      (-> character
                          (assoc :career career-key)
                          (dissoc :specialization))))))

(reg-event-db
 ::set-specialization
 [local-save-character]
 (fn-traced [db [_ specialization-key]]
            (assoc-in db [:character :specialization] specialization-key)))

(reg-event-db
 ::set-skill-rank
 [local-save-character]
 (fn-traced [db [_ skill-key skill-value]]
            (assoc-in db
                      [:character :skills skill-key]
                      (max 0 (min skill-value 6)))))

(defn remove-item [v index]
  (vec
   (keep-indexed
    (fn [i item]
      (when (not= i index)
        item))
    v)))

(defn remove-value [v value]
  (remove
   #(= value %)
   v))

(reg-event-db
 ::set-character-sheet-tab
 (fn-traced [db [_ tab-key]]
            (assoc db :character-sheet-tab tab-key)))

(reg-event-db
 ::add-additional-specialization
 [local-save-character]
 (fn-traced [db [_ spec-key]]
            (update-in db
                       [:character :additional-specializations]
                       (fn [specs]
                         (let [specs (or specs [])]
                           (conj specs spec-key))))))

(reg-event-db
 ::remove-additional-specialization
 [local-save-character]
 (fn-traced [db [_ key]]
            (update-in db
                       [:character :additional-specializations]
                       remove-value
                       key)))

(reg-event-db
 ::set-additional-specialization
 [local-save-character]
 (fn-traced [db [_ index specialization-key]]
            (assoc-in db
                      [:character :additional-specializations index]
                      specialization-key)))

(reg-event-db
 ::add-talent
 [local-save-character]
 (fn-traced [db [_ specialization-key talent-key]]
            (update-in db
                       [:character :talents specialization-key]
                       (fn [talents]
                         (conj (or talents #{}) talent-key)))))

(reg-event-db
 ::remove-talent
 [local-save-character]
 (fn-traced [db [_ specialization-key talent-key]]
            (update-in db
                       [:character :talents specialization-key]
                       disj
                       talent-key)))

(reg-event-db
 ::set-talent
 [local-save-character]
 (fn-traced [db [_ specialization-key index talent-key]]
            (assoc-in db
                      [:character :talents specialization-key index]
                      talent-key)))

(reg-event-fx
 ::set-localstore
 (fn-traced [ctx [_ key value]]
            (.setItem js/localStorage
                      key
                      (clj->js value))
            ctx))

(reg-event-fx
 ::set-username
 (fn-traced [{:keys [db]} [_ username]]
            {:dispatch [::set-localstore "username" username]
             :db (assoc db :username username)}))

(reg-event-fx
 ::set-jwt-token
 (fn-traced [{:keys [db]} [_ jwt-token]]
            {:dispatch [::set-localstore "jwt-token" jwt-token]
             :db (assoc db :jwt-token jwt-token)}))

(reg-event-db
 ::set-auth
 (fn-traced [db [_ auth]]
            (assoc db :auth auth)))

(reg-event-fx
 ::login-success
 (fn [db [_ auth result]]
   (js/console.log "RESULT" result)
   (let [access-token (.-accessToken result)
         _ (js/console.log access-token)
         id-token (.-idToken result)
         _ (js/console.log "ID TOKEN" id-token)
         jwt-token (.getJwtToken id-token)
         _ (prn "JWT TOKEN" jwt-token)
         username (.getUsername access-token)]
     {:dispatch-n [[::set-username username]
                   [::set-jwt-token jwt-token]]})))

(reg-event-fx
 ::login
 [(inject-cofx :auth)]
 (fn [cofx [_ a]]
   (let [auth (:auth cofx)]
     (.getSession auth)
     {})))

(reg-event-fx
 ::logout
 [(inject-cofx :auth)]
 (fn-traced [cofx _]
            (.signOut (:auth cofx))
            (.removeItem js/localStorage "username")
            (.removeItem js/localStorage "jwt-token")
            {:db (dissoc (:db cofx) :username :jwt-token :auth)}))

(reg-event-db
 :character/set-experience-points
 [local-save-character]
 (fn [db [_ v]]
   (assoc-in db [:character :experience-points] v)))

(reg-event-db
 :character/offset-experience-points
 [local-save-character]
 (fn [db [_ v]]
   (update-in db [:character :experience-points] (fn [xps] (+ (or xps 0) v)))))

(reg-event-db
 :character/save-success
 (fn [db [_ arg]]
   (prn "SUCCESS" arg)
   db))

(reg-event-fx
 ::handle-auth-failure
 (fn [_ [_ resp]]
   (let [status (get resp :status)]
     (prn "HANDLE AUTH FAILURE" status resp)
     (cond-> {}
       (= 401 status) (assoc :dispatch [::logout])))))

(reg-event-fx
 :character/save-failure
 (fn [_ [_ resp]]
   (prn "FAILED" resp)
   {:dispatch [::handle-auth-failure resp]}))

(reg-event-fx
 :character/save-character
 (fn [{:keys [db]}]
   (let [character (get db :character)
         id (or (:id character) (str (random-uuid)))
         user-id (get db :username)]
     {:http-xhrio {:method :put
                   :uri "https://r2ua989g93.execute-api.us-east-1.amazonaws.com/v1/characters"
                   :headers {"Authorization" (str "Bearer " (get db :jwt-token))
                             "Content-Type" "application/json"}
                   :body (js/JSON.stringify (clj->js (assoc character
                                                            :id id
                                                            :user-id user-id)))
                   :format (ajax/json-request-format)
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success [:character/save-success]
                   :on-failure [:character/save-failure]}})))

(reg-event-db
 :character/get-characters-success
 (fn [db [_ resp]]
   (prn "RESP" resp)
   (js/console.log (clj->js resp))
   (prn "SUCCESS" (mapv
                   (fn [v]
                     (js->clj
                      (aws/DynamoDB.Converter.unmarshall (clj->js v))
                      :keywordize-keys true))
                   resp))
   db))

(reg-event-fx
 :character/get-characters-failure
 (fn [_ [_ resp]]
   (prn "FAILURE" resp)
   {:dispatch [::handle-auth-failure resp]}))

(reg-event-fx
 :character/get-characters
 (fn [{:keys [db]}]
   {:http-xhrio {:method :get
                 :uri "https://r2ua989g93.execute-api.us-east-1.amazonaws.com/v1/characters"
                 :headers {"Authorization" (str "Bearer " (get db :jwt-token))
                           "Content-Type" "application/json"}
                 :response-format (ajax/json-response-format {:keywords? false})
                 :on-success [:character/get-characters-success]
                 :on-failure [:character/get-characters-failure]}}))
