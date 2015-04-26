(ns quartzite-example.scheduler
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.conversion :as qc]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.simple :as s]
            [clj-time.core :as time]))

(def triggered-counter (atom 0))

(defjob SimpleJob [ctx]
  (let [data (qc/from-job-data ctx)
        message (data "message")]
    (swap! triggered-counter inc)
    (println message)))

(defn- build-trigger [seconds]
  (t/build
    (t/start-at (time/plus (time/now) (time/seconds 2)))))

(defn- build-job [message]
  (j/build
    (j/of-type SimpleJob)
    (j/using-job-data {"message" message})))

(defn- build-scheduler []
  (println "building scheduler")
  (-> (qs/initialize)qs/start))

(def scheduler (memoize build-scheduler))

(defn schedule [message seconds]
  (let [trigger (build-trigger seconds)
        job     (build-job message)]
    (qs/schedule (scheduler) job trigger)))

(defn init []
  (println "starting quartzite")
  (scheduler))
