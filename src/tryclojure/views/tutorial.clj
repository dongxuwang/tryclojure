(ns tryclojure.views.tutorial
  (:require [clojure.java.io :as io]))

(defn tutorial-html [page] 
  (slurp (io/resource (str "public/tutorial/" page ".html"))))
