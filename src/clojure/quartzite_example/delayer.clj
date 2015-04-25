(ns quartzite-example.delayer
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.simple :refer [schedule repeat-forever with-interval-in-seconds]]))

(defjob SimpleJob [ctx]
  (println "scheduled task triggered"))

(defn- build-trigger []
  (t/build
    (t/with-identity (t/key "triggers.1"))
    (t/start-now)
    (t/with-schedule (schedule (repeat-forever) (with-interval-in-seconds 2)))))

(defn- build-job []
  (j/build
    (j/of-type SimpleJob)
    (j/with-identity (j/key "jobs.noop.1"))))

(defn init []
  (println "starting quartzite")
  (let [scheduler (-> (-> (qs/initialize)qs/start))
        trigger   (build-trigger)
        job       (build-job)]
    (qs/schedule scheduler job trigger)))
