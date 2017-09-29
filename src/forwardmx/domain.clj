(ns forwardmx.domain
  (:require
   [forwardmx.core :as fmx]
   ))

(defn get-all
  "Retrieve an array of domains and aliases"
  []
  (fmx/get "/domains"))

(defn create!
  ""
  [domain]
  (fmx/set! "/domain/create" {"domain" domain}))

(defn destroy!
  ""
  [domain]
  (fmx/set! "/domain/destroy" {"domain" domain}))

;;(create! "x.y.com")
;;(destroy! "x.y.com")

