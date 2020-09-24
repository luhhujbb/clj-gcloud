# clj-gcloud

A clojure library to interact with google compute engine

## Usage :

All list-all function are lazy

### Compute Instance

```clojure
(:require [clj-gcloud.compute.instance :as instance])

;;initialize compute client
(def client (instance/init {:json-path "/home/user/.gcloud/creds.json"})))

(def instances-list (instance/list-all client "project" "zone"))

(instance/stop client project zone instance-id)
```

### Sql Admin


```clojure
(:require [clj-gcloud.sql.sql-admin :as sql-admin])

;;initialize sql-admin client
(def client (sql-admin/init {:json-path "/home/user/.gcloud/creds.json"})))

;;list all cloud-sql instances
(def instances-list (sql-admin/list-all client "project" "zone"))

(instance/restart client project instance-id)
```

### Storage


```clojure
(:require [clj-gcloud.storage.core :as storage]
          [clj-gcloud.storage.bucket :as bucket]
          [clj-gcloud.storage.object :as object)

;;initialize storage client
(def client (storage/init {:json-path "/home/user/.gcloud/creds.json" :application-name "script-name"})))

;;list all buckets
(def bucket-list (bucket/list-all client project))

;;create a bucket
(bucket/create client
  project
  { :name "bucket-name"
    :storage-class "STANDARD"
    :location "europe-west1"})

(bucket/delete client project instance-id)

;;list all-objects
(def object-list (object/list-all client project))

;;list all-objects with a prefix
(def object-list (object/list-all client project {:prefix "my-prefix/my-sub-prefix/"}))

;;put a string into gcs with eventual content-type
(object/put-string client bucket key "mystring to store")
(object/put-string client bucket key "{\"test\":\"dev\"}" {:content-type "application-json"})

;;get a string
(object/get-string client bucket key)

;;put a file on gcs
(object/put-file client bucket key "/path/to/a/file/to/store/on/gcs.txt")
;;store a file from gcs
(object/get-file client bucket key "/path/to/a/file/to/store/on/gcs.txt")

;;copy object
(object/copy client bucket key dst-bucket dst-key)

;;delete
(object/delete client bucket key)
```

## License

Copyright © 2020 Jean-Baptiste Besselat

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
