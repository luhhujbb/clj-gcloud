(ns clj-gcloud.sql.core
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common])
    (:import [com.google.api.services.sql SQLAdmin SQLAdmin$Builder]
             [com.google.api.client.googleapis.auth.oauth2 GoogleCredential]
             [com.google.api.client.googleapis.javanet GoogleNetHttpTransport]
             [com.google.api.client.http HttpTransport]
             [com.google.api.client.json JsonFactory]
             [java.util Arrays]))

(defn ^SQLAdmin init
  [options]
  ^SQLAdmin
  (let [^HttpTransport http-transport (common/http-transport)
        ^JsonFactory json-factory (common/jackson-default-factory)
        ^GoogleCredential credential (common/mk-credentials (:json-path options))
        builder (SQLAdmin$Builder. http-transport json-factory credential)
        builder (if (:application-name options)
                  (.setApplicationName builder (:application-name options))
                  builder)]
    (.build builder)))
