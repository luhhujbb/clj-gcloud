(ns clj-gcloud.storage.core
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common]
            [cheshire.core :as json]
            [clojure.java.io :as io])
    (:import [com.google.api.services.storage
                Storage
                Storage$Builder]
             [com.google.api.services.storage.model
                Bucket
                Buckets]
             [java.util Arrays]))


(defn ^Storage init
 [options]
 ^Storage
  (common/build-service Storage$Builder
    (if-not (:scope options)
      (assoc options :scope ["https://www.googleapis.com/auth/devstorage.read_write"])
      options)))

;;bucket

(defn list-buckets
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

(defn list-all-buckets
  [client project-id]
  (lazy-seq
    (loop [buckets-list []
           response (list-buckets client project zone)]
           (if (some? (:next-page-token response))
              (recur
                (into [] (concat buckets-list (:items response)))
                (list client project zone (:next-page-token response)))
              (into [] (concat instances-list (:items response)))))))

(defn create-bucket
  [client project-id bucket-name & [options])

(def get-bucket-info
  [client project-id bucket-name])

(def delete-bucket
  [client project-id bucket-name])

;;objects
(defn put-file
  [client bucket key file-path])

(defn put-string
  [client bucket key content-string])

(defn put-object
  [client bucket key input-stream])


(defn get-object
  [client bucket key])

(defn get-string
  [client bucket key])

(defn get-file
  [client bucket key file-path])

(defn delete-object
  [client bucket key])
