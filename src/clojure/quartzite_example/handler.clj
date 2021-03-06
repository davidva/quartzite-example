(ns quartzite-example.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :refer [resource-response content-type redirect]]
            [quartzite-example.scheduler :refer [schedule triggered-counter]]
            [cheshire.core :refer [generate-string]]))

(defroutes app-routes
  (GET "/" [] (-> (resource-response "index.html" {:root "public"}) (content-type "text/html")))
  (POST "/schedule" {params :params}
    (schedule (params :message) 2)
    {:status 200})
  (GET "/counter" [] (-> {:status 200 :body (generate-string {:counter @triggered-counter})} (content-type "application/json")))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
