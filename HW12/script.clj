(defn operator [appfn args] 
	(fn [vars] 
		(println args)))

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
	(fn [vars]
		(get vars nam 0)))

(defn get-brak [s] 
	(loop [ind 0 lastind 0 bb 0 ret []]
		(if (>= ind (.length s))
			(if (= lastind ind) ret (conj ret (subs s lastind ind)))
			(if (= (get s ind) \()
				(recur (inc ind) lastind (inc bb) ret)
				(if (= (get s ind) \))
					(recur (inc ind) lastind (dec bb) ret)
					(if (and (= bb 0) (= (get s ind) \space))
						(recur (inc ind) (inc ind) bb (if (= lastind ind) ret (conj ret (subs s lastind ind))))
						(recur (inc ind) lastind bb ret)))))))

(defn parseFunction [expr]
	(do (println (str "expr " expr))
	(let [operators {"+" add "-" subtract "*" multiply "/" divide}]
		(let [exp (str (nth expr 0))]
			(do (println exp)
			(if-let [subexpr (re-matches #"\((.*)\)" exp)]
				(parseFunction (get-brak (get subexpr 1)))
				(if (or (re-matches #"-?[0-9]+" exp) (re-matches #"\[a-zA-z]+" exp))
					(map (fn [a] (if (re-matches #"-?[0-9]+" a) (const (read-string a)) (variable a))) expr)
					(if (contains? operators exp)
						(apply (get operators exp) (parseFunction (rest expr)))
						nil))))))))

; (defn parseFunction [expr] 
; 	(let [get-token (fn [exp] 
; 			(re-find #"^(\(|\)|-?[0-9]+|\+|\-|\*|\/)\s*(.*)$" exp))
; 		  number (fn [toks]
; 		  	(if (re-match #"-?[0-9]+" (get toks 1))
; 		  		(const (read-str))))]
; 				nil
; 				nil)
; 		  )]
; 		(get-token expr)))

(println (parseFunction (get-brak "+ (+ 1 2 5) 5")))