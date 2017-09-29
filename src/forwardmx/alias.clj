(ns forwardmx.alias
  (:require
   [forwardmx.core :as fmx]
   ))

(defn get-all
  "Retrieve an array of  aliases for a domain"
  [domain]
  (fmx/get "/aliases" {"domain" domain}))

(defn create!
  "Create an alias for a domain. 
   Leave =alias= as an empty string for catchall."
  [domain alias destination]
  (fmx/set! "/alias/create" { "domain" domain
                             "alias" alias
                             "destination" destination}))

(defn update!
  "Update an alias for a domain.
   Leave =alias= blank to update the catchall"
  [domain alias destination]
  (fmx/set! "/alias/update" {"domain" domain
                             "alias" alias
                             "destination" destination}))

(defn destroy!
  "Destroy an alias in a domain"
  [domain alias]
  (fmx/set! "/alias/destroy" {"domain" domain
                              "alias" alias}))





