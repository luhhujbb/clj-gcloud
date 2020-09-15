(ns clj-gcloud.compute.core
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common])
    (:import [com.google.api.services.compute Compute Compute$Builder]
             [com.google.api.services.sql SQLAdmin SQLAdmin$Builder]
             [com.google.api.client.googleapis.auth.oauth2 GoogleCredential]
             [com.google.api.client.http HttpTransport]
             [com.google.api.client.json JsonFactory]
             [com.google.api.client.json.jackson2 JacksonFactory]
             [java.util Arrays]))

(defn ^Compute init
  [options]
    ^Compute
    (let [^HttpTransport http-transport (common/http-transport)
          ^JsonFactory json-factory (common/jackson-default-factory)
          ^GoogleCredential credential (common/mk-credentials (:json-path options))
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
