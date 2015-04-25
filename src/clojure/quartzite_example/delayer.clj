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

(defn- build-trigger [seconds]
  (t/build
    (t/start-now)
    (t/with-schedule (s/schedule (s/repeat-forever) (s/with-interval-in-seconds seconds)))))

(defn- build-job [message]
  (j/build
    (j/of-type SimpleJob)
    (j/using-job-data {"message" message})))

(defn- build-scheduler []
  (-> (qs/initialize)qs/start))

(def scheduler (memoize build-scheduler))

(defn schedule [message seconds]
  (let [trigger (build-trigger seconds)
        job     (build-job message)]
    (qs/schedule (scheduler) job trigger)))

(defn init []
  (println "starting quartzite")
  (schedule "every 2 seconds" 2)
  (schedule "every 5 seconds" 5))
