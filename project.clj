(defproject luhhujbb/clj-gcloud-compute "1.0.0"
  :description "clojure lib to interact with gcp"
  :url "https://github.com/luhhujbb/clj-gcloud-compute"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.google.cloud/google-cloud-core "1.91.3"]
                 [com.google.cloud/google-cloud-core-http "1.91.3"]
                 [com.google.cloud/google-cloud-compute "0.118.0-alpha"]
                 [com.google.apis/google-api-services-sql "v1beta4-rev20200828-1.30.10"]
                 [org.clojure/tools.logging "0.5.0"]
                 [cheshire "5.9.0"]])
