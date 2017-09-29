# forwardmx

A Clojure library to wrap the API for the forwardmx.io email forwarding service.

forwardmx.io provides a simple service for forwarding mail for multiple domains.

This library simplifies using it from clojure and, in particular, provides a single function
to populate forwardmx.io from a Postfix `virtual` file.


## Usage

You will need to set up an API key where it can be found by the [environ](https://github.com/weavejester/environ) library. For example it could simply be in the environment as `API_KEY`.

``` clojure
(ns this.app
  (:require
   [forwardmx.core :as fmx]
   [forwardmx.domain :as domain]   
   [forwardmx.alias :as alias]
   ))
   
   
;; Import the aliases for a particular domain from a Postfix virtual file
(fmx/import-postfix-virtual "virtual" "example.com")

;; Import all aliases  from a Postfix virtual file
(fmx/import-postfix-virtual "virtual")

(domain/create! "x.y.com")
(alias/create! "x.y.com" "ian" "ian@otherdomain.com" )
(alias/update! "x.y.com" "" "ian@domatogetcatchall.com" ) ;; catchall address for this domain

(alias/update! "x.y.com" "ian" "janine@example.com")

(alias/destroy! "x.y.com" "ian")

(domain/destroy! "x.y.com")



```

## License

Copyright © 2017 

Distributed under the Eclipse Public License either version 1.0 or any later version.
