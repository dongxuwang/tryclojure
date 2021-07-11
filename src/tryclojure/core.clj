(ns tryclojure.core
  (:gen-class)
  (:require [tryclojure.routes :refer [view-routes ajax-routes]]
            [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.coercion.schema]
            [ring.middleware.session :as session]
            [org.httpkit.server :refer [run-server]]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                          coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]))

(def app
  (ring/ring-handler
    (ring/router
      [["/" view-routes]
       ["/eval.json" ajax-routes]]
      {:data {:coercion   reitit.coercion.schema/coercion
              :muuntaja   m/instance
              :middleware [parameters-middleware
                           format-negotiate-middleware
                           format-response-middleware
                           exception-middleware
                           format-request-middleware
                           coerce-exceptions-middleware
                           coerce-request-middleware
                           coerce-response-middleware]}})
    (ring/routes
      (ring/redirect-trailing-slash-handler)
      (ring/create-resource-handler {:path "/"})
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body "Route not found"})}))
    {:middleware [session/wrap-session]}))

(defn -main []
  (println "Server started")
  (run-server app {:port 4000}))
