(ns quartzite-example.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [resource-response content-type redirect]]))

(defroutes app-routes
  (GET "/" [] (-> (resource-response "index.html" {:root "public"}) (content-type "text/html")))
  (POST "/schedule" []
    (println "scheduled")
    (redirect "/"))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
