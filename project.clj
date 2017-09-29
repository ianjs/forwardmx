(defproject forwardmx "0.1.0"
  :description "Access the forwardmx API to create, edit and delete domains and email redirections"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-environ "1.1.0"]
            [lein-pprint "1.1.2"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ "1.1.0"]
                 [clj-http "3.7.0"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/data.json "0.2.6"]
                 ]
  :repl-options {:init-ns forwardmx.core}
  )
