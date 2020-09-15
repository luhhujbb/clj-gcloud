(ns clj-gcloud.common.core
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io])
    (:import
             [com.google.api.client.googleapis.auth.oauth2 GoogleCredential]
             [com.google.api.client.googleapis.javanet GoogleNetHttpTransport]
             [com.google.api.client.http HttpTransport]
             [com.google.api.client.json JsonFactory]
             [com.google.api.client.json.jackson2 JacksonFactory]
             [java.util Arrays]))

(defn mk-credentials
   [json-path]
   (GoogleCredential/fromStream (io/input-stream json-path)))

(defn jackson-default-factory
  []
  ^JsonFactory (JacksonFactory/getDefaultInstance))

(defn http-transport
  []
  ^HttpTransport (GoogleNetHttpTransport/newTrustedTransport))
