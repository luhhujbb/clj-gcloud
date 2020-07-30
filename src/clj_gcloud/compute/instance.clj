(ns clj-gcloud.compute.instance
  (:require [clojure.tools.logging :as log])
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
    [^Compute client project zone]
    (let [^Compute$Instances$List request (.list (.instances client) project zone)
          ^InstanceList reponse (.execute request)]
          ))

(defn get
    [^Compute client project zone instance-id]
    (let [^Compute$Instances$Get request (.get (.instances client) project zone instance-id)
          ^Instance reponse (.execute request)]))

(defn stop
  [^Compute client project zone instance-id]
    (-> (.instances client)
      (.Stop project zone instance-id)
      (.execute)))

(defn start
  [^Compute client project zone instance-id]
  (-> (.instances client)
    (.Start project zone instance-id)
    (.execute)))

(defn reset
  [^Compute client project zone instance-id]
  (-> (.instances client)
    (.Reset project zone instance-id)
    (.execute)))

(defn delete
  [^Compute client project zone instance-id]
  (-> (.instances client)
    (.Delete project zone instance-id)
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
