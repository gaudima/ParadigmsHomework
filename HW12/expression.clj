(use 'clojure.string)
(defn operator [appfn args] 
  (fn [vars] 
    (appfn (map (fn [a] (a vars)) args))))

(defn add [& args]
  (operator (fn [a] (reduce + a)) args))

(defn subtract [& args]
  (operator (fn [a] (reduce - a)) args))

(defn multiply [& args]
  (operator (fn [a] (reduce * a)) args))

(defn divide [& args]
  (operator (fn [a] (try
                      (reduce / a)
                      (catch Exception e (/ 1.0 0.0)))) args))

(defn negate [arg]
  (fn [vars] (- (arg vars))))

(defn constant [v]
  (fn [vars] (double v)))

(defn variable [nam]
  (fn [vars] (double (get vars nam))))

(defn parseFunction [expression]
  (let [operators {"+" add "-" subtract "*" multiply "/" divide}
        un-operators {"negate" negate}
        get-tok (fn [s]
                  (loop [ind 0 bb 0]
                    (if (= (str (get s ind)) "(")
                      (recur (inc ind) (inc bb))
                      (if (= (str (get s ind)) ")")
                        (recur (inc ind) (dec bb))
                        (if (or (and (= bb 0) (re-matches #"\s" (str (get s ind)))) (>= ind (.length s)))
                          (list (subs s 0 ind) (nth (re-find #"\s*(.*)\s*" (subs s ind (.length s))) 1))
                          (recur (inc ind) bb))))))
        expr (get-tok (trim expression))]
    (if (contains? operators (first expr))
      (apply (get operators (first expr))
             (loop [exp (second expr) ret []]
               (let [ex (get-tok exp)]   
                 (if (= (first ex) "")
                   ret
                   (recur (second ex) (conj ret (parseFunction (first ex))))))))
      (if (contains? un-operators (first expr))
        ((get un-operators (first expr)) (parseFunction (second expr)))
        (if-let [subex (re-matches #"\((.+)\)" (first expr))]
          (parseFunction (nth subex 1))
          (if (re-matches #"-?[0-9]+\.?[0-9]*" (first expr))
            (constant (read-string (first expr)))
            (variable (first expr))))))))

(println ((divide (constant 2.0) (constant 0.0)){}))
