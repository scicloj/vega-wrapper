(ns scicloj.viz.dataset
  (:require [tech.v3.dataset.impl.dataset]
            [tech.v3.dataset :as tmd]
            [scicloj.tempfiles.api :as tempfiles]))

(defn dataset? [data]
  (instance? tech.v3.dataset.impl.dataset.Dataset data))

(defn dataset->url [dataset]
  (let [{:keys [path route]} (tempfiles/tempfile! ".csv")]
    (tmd/write! dataset path)
    route))

(defn dataset-like? [data]
  (or (and (sequential? data)
           (every? map? data))
      (and (map? data)
           (every? sequential? data))))

(defn throw-if-column-missing [viz-map column-name]
  (let [dataset (:metamorph/data viz-map)]
    (if dataset
      (if (get dataset column-name)
        viz-map
        (throw (ex-info "Missing column"
                        {:column-name column-name})))
      ;; no data -- nothing to check
      viz-map)))
