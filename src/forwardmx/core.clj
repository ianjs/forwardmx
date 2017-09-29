(ns forwardmx.core
  (:require
   [environ.core :refer [env]]
   [clj-http.client :as client]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.string :as str]
   )
  )

(def api-url "https://forwardmx.io/api")
(def api-key (env :api-key))

(defn- fmx-call-url
  "Build the url for a call"
  [command options]
  (str api-url 
       command
       "?key=" api-key "&"
       (clojure.string/join "&"
                            (map #(str % "=" (options %)) (keys options) )))
  )


(defn get
  "Make a call to forwardmx to get some info"
  [command & [options]]
  (json/read-str (:body (client/get (fmx-call-url command options)))))

(defn set!
  "Make a call to forwardmx to change something"
  [command & [options]]
  (let [url (fmx-call-url command options)
        result (json/read-str (:body (client/get url)))]
    (if (result "error")
      (throw (Exception. (str "API error:" result "\n" url)))
      result)))

(defn- create-postfix-virtual-entry
  ""
  [from-to]
  (let [[_ user domain] (re-find #"^(.*)@(.*)$" (first from-to))] ;; Parse out the bits
    (if (empty? ;; Make sure alias doesn't already exist
         (filter #(= (str user "@") % )
                 (map #(% "source")
                      (forwardmx.alias/get-all domain))))
      (do
        (println "Creating" (str user "@" domain) "...")
        (forwardmx.alias/create! domain user (second from-to)))
      (do
        (println "Updating" (str user "@" domain) "...")
        (forwardmx.alias/update! domain user (second from-to))))))


(defn import-postfix-virtual
  
  "Parse a postfix =virtual= file from any stream supported by clojure.java.io/input-stream.

  If a domain is specified then import just that domain's aliases."
  
  [instream & [domain]]
  (let [domain-pattern (re-pattern (str "(?i)@" domain))
        current-domains (forwardmx.domain/get-all)
        from-to (as-> instream i ;; Pull in a list of from/to pairs for aliases
                  (slurp (io/input-stream i))
                  (str/split (str/lower-case i) #"\n") ;; split into lowercase lines
                  (filter #(not (re-find #"^\s*#" %)) i) ;; No comments
                  (map #(str/split % #"\s+") i) ;; split into from/to mapping
                  (filter #(= (count %) 2) i) ;; Remove anything that doesn't look like a mapping eg blank lines
                  (filter #(re-find #"@" (first %)) i) ;; ignore 'dummy' markers for domains postfix will handle
                  (if domain (filter #(re-find domain-pattern (first %)) i) i) ;; Filter down to one domain if specified
                  )
        existing-domains (set (map #(% "domain") (forwardmx.domain/get-all)))
        domains-to-import ( set (map #(second (re-matches #"^.*@(.+)$" (first %))) from-to) )
        domains-to-create (clojure.set/difference domains-to-import existing-domains)]
    (doall
     (map #(do
             (println "Creating domain " % "...")
             (forwardmx.domain/create! %))
          domains-to-create))
    (doall
     (map #(merge {"from-to" %} (create-postfix-virtual-entry %)) from-to ))))



;; (forwardmx.domain/destroy! "x.y.com")
;; (forwardmx.alias/create! "x.y.com" "ian" "ian@example.com" )
;; (forwardmx.domain/create! "x.y.com")
;; (forwardmx.domain/create! "x.y.com")
;; (forwardmx.alias/create! "x.y.co" "ian" "ian@example.com" )
;; (forwardmx.alias/create! "x.y.com" "ian" "ian@example.com" )
;; (forwardmx.alias/update! "x.y.com" "" "ian@example.com" )

;; (forwardmx.alias/update! "x.y.com" "@x.y.com" "janine@example.com")
;; (forwardmx.alias/update! "x.y.com" "ian" "janine@example.com")

;; (fmx-call-url "/domains" {"domain" "example.com"})

;; (forwardmx.alias/destroy! "x.y.com" "")
;; (forwardmx.alias/destroy! "x.y.com" "")

;; (forwardmx.domain/destroy! "x.y.com")
;; (forwardmx.domain/destroy! "x.y.com")

;;(import-postfix-virtual "virtual" )









