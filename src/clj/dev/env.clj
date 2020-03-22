(ns dev.env
  (:require [clojure.java.io :as io]
            [amazonica.aws.cloudformation :as cf])
  (:gen-class))

(defn -main [& args]
  (let [stack-name (first args)
        region (second args)]
    (prn "STACK NAME" stack-name)
    (prn "REGION" region)
    (prn "AWS PROFILE" (System/getenv "AWS_PROFILE"))
    (let [stacks (cf/describe-stacks
                  {:endpoint region}
                  :stack-name stack-name)
          _ (prn "STACKS" stacks)
          outputs (-> stacks
                      :stacks
                      first
                      :outputs)
          _ (prn "OUTPUTS" outputs)
          env-map (into {}
                        (map (juxt :output-key :output-value))
                        outputs)]
         (with-open [writer (io/writer (io/file "src/cljs/config/env.cljs"))]
           (.write writer (str '(ns config.env
                                  "This file is generated by dev.env")))
           (.write writer (str `(def ~'env-map ~env-map)))))))