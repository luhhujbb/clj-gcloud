(ns clj-gcloud.compute.instance
  (:require [clojure.tools.logging :as log]
            [cheshire.core :as json])
    (:import [com.google.api.services.compute
                Compute
                Compute$Instances
                Compute$Instances$List
                Compute$Instances$Get]
             [com.google.api.services.compute.model
                Instance
                InstanceList
                InstancesSetLabelsRequest]
             [java.util Arrays Map]))

(defn list
    [^Compute client project zone & [next-page-token]]
    (let [^Compute$Instances$List request (.list (.instances client) project zone)
          ^Compute$Instances$List request (if (some? next-page-token)
                                            (.setPageToken request next-page-token)
                                            request)
          ^InstanceList response (.execute request)]
          {:items (json/parse-string (.toString (.getItems response)) true)
           :next-page-token (.getNextPageToken response)}))

(defn list-all
  [^Compute client project zone]
    (lazy-seq
      (loop [instances-list []
             response (list client project zone)]
             (if (some? (:next-page-token response))
                (recur
                  (into [] (concat instances-list (:items response)))
                  (list client project zone (:next-page-token response)))
                (into [] (concat instances-list (:items response)))))))

(defn get
    [^Compute client project zone instance-id]
    (let [^Compute$Instances$Get request (.get (.instances client) project zone instance-id)
          ^Instance response (.execute request)]
          (json/parse-string (.toString response) true)))

(defn stop
  [^Compute client project zone instance-id]
    (-> (.instances client)
      (.stop project zone instance-id)
      (.execute)))

(defn start
  [^Compute client project zone instance-id]
  (-> (.instances client)
    (.start project zone instance-id)
    (.execute)))

(defn reset
  [^Compute client project zone instance-id]
  (-> (.instances client)
    (.reset project zone instance-id)
    (.execute)))

(defn delete
  [^Compute client project zone instance-id]
  (-> (.instances client)
    (.delete project zone instance-id)
    (.execute)))

(defn- build-labels-request
  ^InstancesSetLabelsRequest
  [labels]
  (let [^Map str-map (clojure.walk/stringify-keys labels)]
    (.setLabels (InstancesSetLabelsRequest.) str-map)))

(defn set-labels
  [^Compute client project zone instance-id labels]
    (let [^InstancesSetLabelsRequest labels-req (build-labels-request labels)]
    (-> (.instances client)
      (.setLabels project zone instance-id labels-req)
      (.execute))))