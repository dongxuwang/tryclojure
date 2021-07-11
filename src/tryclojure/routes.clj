(ns tryclojure.routes
  (:require [schema.core :as s]
            [tryclojure.views.home :as home]
            [tryclojure.views.tutorial :as tutorial]
            [tryclojure.models.eval :refer [eval-request make-sandbox]]))

(defn tutorial
  [{:keys [parameters]}]
  (let [data (:form parameters)]
    {:status 200
     :body   (tutorial/tutorial-html (data :page))}))

(def view-routes
  [""
   ["about" {:get (fn [_]
                    {:status  200
                           :headers {"Content-Type" "text/html"}
                           :body    (home/about-html)})}]
   ["links" {:get (fn [_] {:status  200
                           :headers {"Content-Type" "text/html"}
                           :body    (home/links-html)})}]
   ["tutorial" {:post {:parameters {:form {:page s/Str}}
                       :handler    tutorial}}]
   ["" {:get (fn [_]
               {:status  200
                :headers {"Content-Type" "text/html"}
                :body    (home/root-html)})}]])


(defn eval-json [expr sb]
  (let [{:keys [expr result error message] :as res} (eval-request expr sb)
        data (if error
               res
               (let [[out res] result]
                 {:expr (pr-str expr)
                  :result (str out (pr-str res))}))]
    data))

(defn do-eval
  [{:keys [parameters session]}]
  (let [data (:query parameters)
        sandbox (or (:sandbox session) (make-sandbox))]
    {:status 200
     :body   (eval-json (data :expr) sandbox)
     :session {:sandbox sandbox}}))


(def ajax-routes
  [""
   ["" {:get  {:parameters {:query {:expr s/Str}}
               :handler    do-eval}
        :post {:parameters {:body {:expr s/Str}}
               :handler    do-eval}}]])

