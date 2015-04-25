(ns quartzite-example.delayer
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.conversion :as qc]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.simple :as s]))

(defjob SimpleJob [ctx]
  (let [data (qc/from-job-data ctx)
        message (data "message")]
    (println message)))

(defn- build-trigger []
  (t/build
    (t/with-identity (t/key "triggers.1"))
    (t/start-now)
    (t/with-schedule (s/schedule (s/repeat-forever) (s/with-interval-in-seconds 2)))))

(defn- build-job [message]
  (j/build
    (j/of-type SimpleJob)
    (j/using-job-data {"message" message})
    (j/with-identity (j/key "jobs.noop.1"))))

(defn- build-scheduler []
  (-> (qs/initialize)qs/start))

(def scheduler (memoize build-scheduler))

(defn schedule [message]
    (let [trigger (build-trigger)
          job     (build-job message)]
    (qs/schedule (scheduler) job trigger)))

(defn init []
  (println "starting quartzite")
  (schedule "every 2 seconds"))
