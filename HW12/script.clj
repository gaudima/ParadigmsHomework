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
  (operator (fn [a] (reduce / a)) args))

(defn const [v]
  (fn [vars] v))

(defn variable [nam]
  (fn [vars] (get vars nam 0)))

(defn parseFunction [expression]
  (let [operators {"+" add "-" subtract "*" multiply "/" divide}
        get-tok (fn [s]
                  (loop [ind 0 bb 0]
                    (if (= (str (get s ind)) "(")
                      (recur (inc ind) (inc bb))
                      (if (= (str (get s ind)) ")")
                        (recur (inc ind) (dec bb))
                        (if (or (and (= bb 0) (re-matches #"\s" (str (get s ind)))) (>= ind (.length s)))
                          (list (subs s 0 ind) (nth (re-find #"\s*(.*)\s*" (subs s ind (.length s))) 1))
                          (recur (inc ind) bb))))))
        expr (get-tok expression)]
    (if (contains? operators (first expr))
      (apply (get operators (first expr))
             (loop [exp (second expr) ret []]
               (let [ex (get-tok exp)]
                 (if (= (first ex) "")
                   ret
                   (if-let [subex (re-matches #"\((.+)\)" (first ex))]
                     (recur (second ex) (conj ret (parseFunction (nth subex 1))))
                     (if (re-matches #"-?[0-9]+\.?[0-9]*" (first ex))
                       (recur (second ex) (conj ret (const (read-string (first ex)))))
                       (recur (second ex) (conj ret (variable (first ex))))))))))
      (if-let [subex (re-matches #"\((.+)\)" (first expr))]
        (parseFunction (nth subex 1))
        nil))))

(println ((parseFunction "(/ (* 2.0 (+ 1 2 3)) x)") {"x" 5}))
