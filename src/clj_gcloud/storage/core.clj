(ns clj-gcloud.storage.core
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common]
            [clojure.java.io :as io])
    (:import [com.google.api.services.storage Storage Storage$Builder]
             [java.util Arrays]))


(defn ^Storage init
 [options]
 ^Storage
  (common/build-service Storage$Builder
    (if-not (:scope options)
      (assoc options :scope ["https://www.googleapis.com/auth/devstorage.read_write"])
      options)))
