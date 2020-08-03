# clj-gcloud-compute

A clojure library to interact with google compute engine

## Usage

```clojure
(:require [clj-gcloud.compute.core :as compute]
          [clj-gcloud.compute.instance :as instance])

;;initialize compute client
(def client (compute/init {:json-path "/home/user/.gcloud/creds.json"})))

(def instances-list (instance/list-all client "project" "zone"))
```

## License

Copyright © 2020 Jean-Baptiste Besselat

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
