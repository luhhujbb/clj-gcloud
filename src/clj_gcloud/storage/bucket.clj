(ns clj-gcloud.storage.bucket
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-gcloud.storage.core :refer [init]])
    (:import [com.google.api.services.storage
                Storage
                Storage$Buckets]
             [com.google.api.services.storage.model
                Bucket
                Buckets]
             [java.util Arrays]))


;;bucket

(defn list
  "List bucket (truncated)"
  [client project-id & [next-page-token]]
  (let [^Storage$Buckets$List request (.list (.buckets client) project)
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
           response (clj-gcloud.storage.bucket/list client project)]
           (if (some? (:next-page-token response))
              (recur
                (into [] (concat buckets-list (:items response)))
                (clj-gcloud.storage.bucket/list client project (:next-page-token response)))
              (into [] (concat buckets-list (:items response)))))))

(defn create-bucket
  "Bucket specs contains at least:
  * name
  * location (region, multiregional)
  * storage-class"
  [client project-id bucket-specs]

  (let [^Bucket bucket (-> (Bucket.)
                          (.setName (:name bucket-specs))
                          (.setLocation (:location bucket-specs))
                          (.setStorageClass (:storage-class bucket-specs)))]
        (.execute (.insert (.buckets client) project-id bucket))))

(def describe-bucket
  "Retrieve bucket info"
  [client bucket-name]
  (json/parse-string
    (.execute
      (.get
        (.buckets client)
        bucket-name))
    true))

(def delete-bucket
  "Delete a bucket"
  [client bucket-name]
  (json/parse-string
    (.execute
      (.delete
        (.buckets client)
        bucket-name))
    true))
