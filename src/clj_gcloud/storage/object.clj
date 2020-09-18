(ns clj-gcloud.storage.object
  (:require [clojure.tools.logging :as log]
            [clj-gcloud.common.core :as common]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [clj-gcloud.storage.core :refer [init]])
    (:import [com.google.api.services.storage
                Storage
                Storage$Objects]
             [com.google.api.services.storage.model
                StorageObject]
             [com.google.api.client.http
                  ByteArrayContent
                  FileContent
                  InputStreamContent]
             [java.util Arrays]))
