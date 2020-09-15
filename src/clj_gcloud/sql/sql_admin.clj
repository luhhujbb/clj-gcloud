(ns clj-gcloud.sql.sql-admin
  (:require [clojure.tools.logging :as log]
            [cheshire.core :as json]
            [clj-gcloud.common.core :as common])
    (:import [com.google.api.services.sql
                SQLAdmin
                SQLAdmin$Builder
                SQLAdmin$Instances
                SQLAdmin$Instances$List
                SQLAdmin$Instances$Get]
             [com.google.api.services.sql.model
                InstancesListResponse
                DatabaseInstance]
             [java.util Arrays Map]))


(defn ^SQLAdmin init
   [options]
   ^SQLAdmin
   (common/build-service SQLAdmin$Builder options))

(defn list
     [^SQLAdmin client project & [next-page-token]]
     (let [^SQLAdmin$Instances$List request (.list (.instances client) project)
           ^SQLAdmin$Instances$List request (if (some? next-page-token)
                                             (.setPageToken request next-page-token)
                                             request)
           ^InstancesListResponse response (.execute request)]
           {:items (json/parse-string (.toString (.getItems response)) true)
            :next-page-token (.getNextPageToken response)}))

(defn list-all
   [^SQLAdmin client project]
     (lazy-seq
       (loop [instances-list []
              response (list client project)]
              (if (some? (:next-page-token response))
                 (recur
                   (into [] (concat instances-list (:items response)))
                   (list client project (:next-page-token response)))
                 (into [] (concat instances-list (:items response)))))))

 (defn restart
   [^SQLAdmin client project instance-id]
   (-> (.instances client)
     (.restart project instance-id)
     (.execute)))
