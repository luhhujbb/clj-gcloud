(ns clj-gcloud.storage.object
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-gcloud.storage.core :refer [init]])
    (:import [com.google.api.services.storage
                Storage
                Storage$Objects
                Storage$Objects$List]
             [com.google.api.services.storage.model
                Objects
                StorageObject]
             [com.google.api.client.http
                  ByteArrayContent
                  FileContent
                  InputStreamContent]
             [java.util Arrays]))


(defn mk-storage-object
  [bucket key]
  ^StorageObject (-> (StorageObject.)
    (.setBucket bucket)
    (.setName key)))

;;objects
(defn put-file
 [client bucket key file-path & [opts]]
 (json/parse-string (.toString
   (.execute
   (.insert
     (.objects client)
     bucket
     (mk-storage-object bucket key)
     (FileContent. (:content-type opts) (io/file file-path))))) true))

(defn put-bytes
  [client bucket key content-bytes & [opts]]
  (json/parse-string (.toString
    (.execute
    (.insert
      (.objects client)
      bucket
      (mk-storage-object bucket key)
      (ByteArrayContent. (:content-type opts) content-bytes)))) true))

(defn put-string
  [client bucket key content-string & [opts]]
  (put-bytes client bucket key (.getBytes content-string) opts))

(defn put
 [client bucket key input-stream & [opts]]
 (json/parse-string (.toString
   (.execute
   (.insert
     (.objects client)
     bucket
     (mk-storage-object bucket key)
     (InputStreamContent. (:content-type opts) input-stream)))) true))

(defn list
 "List object (truncated)"
 [client bucket & [args]]
 (let [^Storage$Objects$List request (.list (.objects client) bucket)
       ^Storage$Objects$List request (if (some? (:prefix args))
                                         (.setPrefix request (:prefix args))
                                         request)
       ^Storage$Objects$List request (if (some? (:max-results args))
                                         (.setMaxResults request (:max-results args))
                                         request)
       ^Storage$Objects$List request (if (some? (:next-page-token args))
                                         (.setPageToken request (:next-page-token args))
                                         request)
       ^Objects response (.execute request)]
       {:items (if-let [items (.getItems response)]
                  (json/parse-string (.toString items) true)
                  [])
        :next-page-token (.getNextPageToken response)}))

(defn list-all
 "List all objects"
 [client bucket & [args]]
 (let [args* (if (some? args) args {})]
   (lazy-seq
     (loop [buckets-list []
          response (clj-gcloud.storage.object/list client bucket args*)]
          (if (some? (:next-page-token response))
             (recur
               (into [] (concat buckets-list (:items response)))
               (clj-gcloud.storage.object/list
                 client
                 bucket
                 (assoc args* :next-page-token (:next-page-token response))))
             (into [] (concat buckets-list (:items response))))))))


(defn get-object-metadata
  [client bucket key]
  (json/parse-string (.toString (.execute (.get (.objects client) bucket key))) true))

(defn get
  [client bucket key]
    (let [baros (new java.io.ByteArrayOutputStream)]
    (.executeMediaAndDownloadTo (.get (.objects client) bucket key) baros)
    baros))

(defn get-string
 [client bucket key]
    (.toString
      (clj-gcloud.storage.object/get client bucket key)))

(defn get-file
 [client bucket key ^String file-path]
 (let [fos (new java.io.FileOutputStream file-path)]
    (.executeMediaAndDownloadTo (.get (.objects client) bucket key) fos)))

(defn delete
 [client bucket key]
 (.execute (.delete (.objects client) bucket key)))
