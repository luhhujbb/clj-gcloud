(ns clj-gcloud.common.core
  (:require [clojure.tools.logging :as log]
            [clojure.java.io :as io])
    (:import
             [com.google.auth.oauth2 GoogleCredentials]
             [com.google.auth.http HttpCredentialsAdapter]
             [com.google.api.client.googleapis.javanet GoogleNetHttpTransport]
             [com.google.api.client.googleapis.services AbstractGoogleClient$Builder]
             [com.google.api.client.http HttpTransport]
             [com.google.api.client.json JsonFactory]
             [com.google.api.client.json.jackson2 JacksonFactory]
             [java.util Arrays]))

(defn mk-credentials
   [json-path]
   (GoogleCredentials/fromStream (io/input-stream json-path)))

(defn jackson-default-factory
  []
  ^JsonFactory (JacksonFactory/getDefaultInstance))

(defn http-transport
  []
  ^HttpTransport (GoogleNetHttpTransport/newTrustedTransport))

(defn build-service
  [^AbstractGoogleClient$Builder service-builder options]
  (log/info options)
  (let [^HttpTransport http-transport (http-transport)
        ^JsonFactory json-factory (jackson-default-factory)
        ^GoogleCredentials credential (mk-credentials (:json-path options))
         ^GoogleCredentials credential (if (.createScopedRequired credential)
                                         (.createScoped
                                           credential (:scope options))
                                         credential)
        ^HttpCredentialsAdapter http-adapter (HttpCredentialsAdapter. credential)
        builder (clojure.lang.Reflector/invokeConstructor
                    service-builder (to-array [http-transport json-factory http-adapter]))
        builder (if (:application-name options)
                  (.setApplicationName builder (:application-name options))
                  builder)]
    (.build builder)))
