(defproject luhhujbb/clj-gcloud-compute "1.0.0"
  :description "clojure lib to interact with gcp"
  :url "https://github.com/luhhujbb/clj-gcloud-compute"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.google.auth/google-auth-library-oauth2-http "0.21.1"]
                 [com.google.auth/google-auth-library-credentials "0.21.1"]
                 [com.google.apis/google-api-services-compute "v1-rev235-1.25.0"]
                 [com.google.apis/google-api-services-sql "v1beta4-rev20200828-1.30.10"]
                 [com.google.apis/google-api-services-storage "v1-rev171-1.25.0"]
                 [org.clojure/tools.logging "0.5.0"]
                 [cheshire "5.9.0"]])
