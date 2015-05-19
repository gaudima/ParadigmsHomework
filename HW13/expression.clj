(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (:prototype obj) key)))

(defn proto-call [obj key & args]
  (apply (proto-get obj key) (cons obj args)))

(defn method [key]
  (fn [obj & args] (apply (partial proto-call obj key) args)))

(defn field [key]
  (fn [obj] (proto-get obj key)))

(defn constructor [ctor prototype]
  (fn [& args] (apply (partial ctor {:prototype prototype}) args)))

;-Binary-
(def Add)
(def Subtract)
(def Multiply)
(def Divide)

;-Unary-
(def Negate)
(def Sin)
(def Cos)

;-Other-
(def Constant)
(def Variable)

;-Methods-
(defn diff [arg v]
  ((method :diff) arg v))

(defn evaluate [arg vars]
  ((method :eval) arg vars))

(defn appl
  ([arg a]
    ((method :apply) arg a))
  ([arg a b]
    ((method :apply) arg a b)))

(defn toString [arg]
  ((method :toString) arg))

;-Fields-
(defn _ops [arg]
  ((field :ops) arg))

(defn _op [arg]
  ((field :op) arg))

;-------
(defn ConstantConst [this value]
  (assoc this 
    :value value))

(def ConstantProto {
  :diff (fn [this v] 
    (Constant 0.0))
  :eval (fn [this vars]
    ((field :value) this))
  :toString (fn [this]
    (str ((field :value) this)))
  })

(def Constant (constructor ConstantConst ConstantProto))

;-------
(defn VariableConst [this nam]
  (assoc this
    :name nam))

(def VariableProto {
  :diff (fn [this v] 
    (if (= v ((field :name) this))
      (Constant 1.0)
      (Constant 0.0)))
  :eval (fn [this vars]
    (get vars ((field :name) this)))
  :toString (fn [this]
    (str ((field :name) this)))
  })

(def Variable (constructor VariableConst VariableProto))

;-------
(defn BinaryConst [this ops o]
  (assoc this
    :ops ops
    :operator o))

(def BinaryProto {
  :eval (fn [this vars]
    (appl this (map (fn [a] (evaluate a vars)) (_ops this))))
  :toString (fn [this]
    (str "(" ((field :operator) this) " " (clojure.string/join " " (map toString (_ops this))) ")"))
  })

(def Binary (constructor BinaryConst BinaryProto))

;-------
(defn AddConst [this & ops]
  (BinaryConst this ops "+"))

(def AddProto
  (assoc BinaryProto
    :diff (fn [this v]
      (apply Add (map (fn [a] (diff a v)) (_ops this))))
    :apply (fn [this args]
      (reduce + args))))

(def Add (constructor AddConst AddProto))

;-------
(defn SubtractConst [this & ops]
  (BinaryConst this ops "-"))

(def SubtractProto
  (assoc BinaryProto
    :diff (fn [this v]
      (apply Subtract (map (fn [a] (diff a v)) (_ops this))))
    :apply (fn [this args]
      (reduce - args))))

(def Subtract (constructor SubtractConst SubtractProto))

;-------
(defn MultiplyConst [this & ops]
  (BinaryConst this ops "*"))

(def MultiplyProto
  (assoc BinaryProto
    :diff (fn [this v]
      (let [f (first (_ops this)) g (if (empty? (rest (_ops this))) (Constant 1.0) (apply Multiply (rest (_ops this))))]
        (Add (Multiply (diff f v) g) (Multiply f (diff g v)))))
    :apply (fn [this args]
      (reduce * args))))

(def Multiply (constructor MultiplyConst MultiplyProto))

;-------
(defn DivideConst [this & ops]
  (BinaryConst this ops "/"))

(def DivideProto
  (assoc BinaryProto
    :diff (fn [this v]
      (let [f (first (_ops this)) g (if (empty? (rest (_ops this))) (Constant 1.0) (apply Divide (rest (_ops this))))]
        (Divide 
          (Subtract 
            (Multiply (diff f v) g) 
            (Multiply f (diff g v))) 
          (Multiply g g))))
    :apply (fn [this args]
      (reduce / args))))

(def Divide (constructor DivideConst DivideProto))

;-------
(defn UnaryConst [this op o]
  (assoc this
    :op op
    :operator o))

(def UnaryProto {
  :eval (fn [this vars]
    (appl this (evaluate (_op this) vars)))
  :toString (fn [this]
    (str "(" ((field :operator) this) " " (toString (_op this)) ")"))
  })

(def Unary (constructor UnaryConst UnaryProto))

;-------
(defn NegateConst [this op]
  (UnaryConst this op "negate"))

(def NegateProto
  (assoc UnaryProto
    :diff (fn [this v]
      (Negate (diff (_op this) v)))
    :apply (fn [this a]
      (- a))))

(def Negate (constructor NegateConst NegateProto))

;-------

(defn CosConst [this op]
  (UnaryConst this op "cos"))

(def CosProto
  (assoc UnaryProto
    :diff (fn [this v]
      (Multiply (Negate (Sin (_op this))) (diff (_op this) v)))
    :apply (fn [this a]
      (Math/cos a))))

(def Cos (constructor CosConst CosProto))

;-------
(defn SinConst [this op]
  (UnaryConst this op "sin"))

(def SinProto
  (assoc UnaryProto
    :diff (fn [this v]
      (Multiply (Cos (_op this)) (diff (_op this) v)))
    :apply (fn [this a]
      (Math/sin a))))

(def Sin (constructor SinConst SinProto))

(defn parseObject [expression]
  (let [bin-op {'+ Add '- Subtract '* Multiply '/ Divide}
        un-op {'negate Negate 'sin Sin 'cos Cos}]
    (cond
      (string? expression) (parseObject (read-string expression))
      (seq? expression)
      (let [exp (first expression)]
        (cond
          (contains? bin-op exp) (apply (get bin-op exp) (map parseObject (rest expression)))
          (contains? un-op exp) ((get un-op exp) (parseObject (second expression)))))
      (or (integer? expression) (float? expression)) (Constant expression)
      (symbol? expression) (Variable (str expression)))))

;-------
;(println (toString (diff (Multiply (Variable "x") (Variable "y") (Variable "z")) "x")))