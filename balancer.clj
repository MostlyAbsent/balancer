(ns balance
  (:require
   [clj-commons.digest :as digest]
   [babashka.process :refer [shell]]
   [clojure.java.io :as io]
   [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  [["-i" "--input DIRECTORY" "Input Directory"
    :parse-fn #(->> %
                    (str (System/getProperty "user.dir") "/")
                    io/file)
    :validate [#(.exists %)]]
   ["-o" "--output DIRECTORY" "Out Directory"
    :parse-fn #(->> %
                    (str (System/getProperty "user.dir") "/")
                    io/file)
    :validate [#(.exists %)]]])

(defn- hash-folder-prefix [f]
  (-> (digest/md5 f)
      (subs 0 2)
      str))

(defn balance-dir [in-d out-d]
  (doseq [f (.list in-d)]
    (let [outpath (str (.getPath out-d) "/" (hash-folder-prefix f))]
      (if (not (.exists (io/file outpath)))
        (shell "mkdir" outpath))

      (shell {:out :string}
             "mv"
             (str (.getPath in-d) "/" f)
             (str outpath "/" f)))))

(let [options (-> (parse-opts *command-line-args* cli-options)
                  :options)]
  (if-let [in-d (:input options)]
    (if-let [out-d (:output options)]
      (balance-dir in-d out-d)
      (prn "Output directory not found"))
   (prn "Input directory not found")))
