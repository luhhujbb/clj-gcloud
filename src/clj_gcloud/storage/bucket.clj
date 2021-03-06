(ns clj-gcloud.storage.bucket
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-gcloud.storage.core :refer [init]])
    (:import [com.google.api.services.storage
                Storage
                Storage$Buckets
                Storage$Buckets$List]
             [com.google.api.services.storage.model
                Bucket
                Buckets]
             [java.util Arrays]))


;;bucket

(defn list
  "List bucket (truncated)"
  [client project-id & [next-page-token]]
  (let [^Storage$Buckets$List request (.list (.buckets client) project-id)
        ^Storage$Buckets$List request (if (some? next-page-token)
                                          (.setPageToken request next-page-token)
                                          request)
        ^Buckets response (.execute request)]
        {:items (if-let [items (.getItems response)]
                   (json/parse-string (.toString items) true)
                   [])
         :next-page-token (.getNextPageToken response)}))

(defn list-all
  "List all buckets"
  [client project-id]
  (lazy-seq
    (loop [buckets-list []
           response (clj-gcloud.storage.bucket/list client project-id)]
           (if (some? (:next-page-token response))
              (recur
                (into [] (concat buckets-list (:items response)))
                (clj-gcloud.storage.bucket/list client project-id (:next-page-token response)))
              (into [] (concat buckets-list (:items response)))))))

(defn create
  "Bucket specs contains at least:
  * name
  * location (region, multiregional)
  * storage-class"
  [client project-id bucket-specs]

  (let [^Bucket bucket (-> (Bucket.)
                          (.setName (:name bucket-specs))
                          (.setLocation (:location bucket-specs))
                          (.setStorageClass (:storage-class bucket-specs)))]
        (json/parse-string
          (.toString
            (.execute (.insert (.buckets client) project-id bucket)))
          true)))

(defn describe
  "Retrieve bucket info"
  [client bucket-name]
  (json/parse-string
    (.toString (.execute
      (.get
        (.buckets client)
        bucket-name)))
    true))

(defn delete
  "Delete a bucket"
  [client bucket-name]
    (.execute
      (.delete
        (.buckets client)
        bucket-name)))
