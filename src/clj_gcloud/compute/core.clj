(ns clj-gcloud.compute.core
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io])
    (:import [com.google.api.services.compute Compute Compute$Builder Compute$Instances Compute$Instances$List]
             [com.google.api.services.compute.model Instance InstanceList]
             [com.google.api.client.googleapis.auth.oauth2 GoogleCredential]
             [com.google.api.client.googleapis.javanet GoogleNetHttpTransport]
             [com.google.api.client.http HttpTransport]
             [com.google.api.client.json JsonFactory]
             [com.google.api.client.json.jackson2 JacksonFactory]
             [java.util Arrays]))

(defn mk-credentials
   [json-path]
   (GoogleCredential/fromStream (io/input-stream json-path)))

(defn ^Compute init
  [options]
    ^Compute
    (let [^HttpTransport http-transport (GoogleNetHttpTransport/newTrustedTransport)
          ^JsonFactory json-factory (JacksonFactory/getDefaultInstance)
          ^GoogleCredential credential (mk-credentials (:json-path options))
          ^GoogleCredential credential (if (.createScopedRequired credential)
                                          (.createScoped
                                            credential
                                            (Arrays/asList
                                              (to-array ["https://www.googleapis.com/auth/cloud-platform"])))
                                          credential)
          builder (Compute$Builder. http-transport json-factory credential)
          builder (if (:application-name options)
                    (.setApplicationName builder (:application-name options))
                    builder)]
      (.build builder)))
