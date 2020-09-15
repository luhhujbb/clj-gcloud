(ns clj-gcloud.storage.core
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common])
    (:import [com.google.api.services.storage Storage Storage$Builder]
             [java.util Arrays]))


(defn ^Storage init
 [options]
 ^Storage
 (common/build-service Storage$Builder options))
