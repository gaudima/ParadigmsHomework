function BinaryOperator(left, right) {
	this.left = left;
	this.right = right;
}

BinaryOperator.prototype.evaluate = function(x, y, z) {
		return this.apply(this.left.evaluate(x, y, z), this.right.evaluate(x, y, z));
};

BinaryOperator.prototype.toString = function() {
	return this.left.toString() + ' ' + this.right.toString() + ' ' + this.operator;
};

BinaryOperator.prototype.simplify = function() {
	//println(this.toString());
	//console.log((this.create().toString() === this.create().simplify().toString()));
	if (this.left instanceof Const && this.right instanceof Const) {
		return new Const(this.evaluate(0,0,0));
	} else if(this.toString() == this.create().toString()){
		return this.create();
	} else {
		return this.create().simplify();
	}
};


//////////////////////
function Const(a) {
	this.value = a;
}

Const.prototype.evaluate = function(x, y, z) {
	return this.value;
};

Const.prototype.toString = function() {
	return '' + this.value;
};

Const.prototype.diff = function(v) {
	return new Const(0);
};

Const.prototype.simplify = function() {
	return new Const(this.value);
};


//////////////////////
function Variable(name) {
	this.name = name;
}

Variable.prototype.evaluate = function(x, y, z) {
	if (this.name == 'x') {
		return x;
	} else if(this.name == 'y') {
		return y;
	} else {
		return z;
	}
};

Variable.prototype.toString = function() {
	return this.name;
};

Variable.prototype.diff = function(v) {
	if (v == this.name) {
		return new Const(1);
	} else {
		return new Const(0);
	}
};

Variable.prototype.simplify = function() {
	return new Variable(this.name);
};
//////////////////////
function Add(left, right) {
	BinaryOperator.call(this, left, right);
	this.operator = '+';
}

Add.prototype = Object.create(BinaryOperator.prototype);
Add.prototype.constructor = Add;

Add.prototype.apply = function(a, b) {
	return a + b;
};

Add.prototype.simplify = function() {
	var ret = BinaryOperator.prototype.simplify.call(this);
	if (this.left.value === 0) {
		return this.right.simplify();
	} else if (this.right.value === 0) {
		return this.left.simplify();
	} else {
		return ret;
	}
};

Add.prototype.create = function() {
	return new Add(this.left.simplify(), this.right.simplify());
};

Add.prototype.diff = function(v) {
	return new Add(this.left.diff(v), this.right.diff(v));
};
/////////////////////
function Subtract(left, right) {
	BinaryOperator.call(this, left, right);
	this.operator = '-';
}

Subtract.prototype = Object.create(BinaryOperator.prototype);
Subtract.prototype.constructor = Subtract;

Subtract.prototype.apply = function(a, b) {
	return a - b;
};

Subtract.prototype.simplify = function() {
	var ret = BinaryOperator.prototype.simplify.call(this);
	if (this.left.value === 0) {
		return (new Negate(this.right.simplify())).simplify();
	} else if (this.right.value === 0) {
		return this.left.simplify();
	} else {
		return ret;
	}
}

Subtract.prototype.create = function() {
	return new Subtract(this.left.simplify(), this.right.simplify());
};

Subtract.prototype.diff = function(v) {
	return new Subtract(this.left.diff(v), this.right.diff(v));
};

/////////////////////
function Multiply(left, right) {
	BinaryOperator.call(this, left, right);
	this.operator = '*';
}

Multiply.prototype = Object.create(BinaryOperator.prototype);
Multiply.prototype.constructor = Multiply;

Multiply.prototype.apply = function(a, b) {
	return a * b;
};

Multiply.prototype.simplify = function() {
	var ret = BinaryOperator.prototype.simplify.call(this);
	if (this.left.value === 0 || this.right.value === 0) {
		return new Const(0);
	} else if (this.right.value === 1) {
		return this.left.simplify();
	} else if (this.left.value === 1) {
		return this.right.simplify();
	} else {
		return ret;
	}
}

Multiply.prototype.create = function() {
	return new Multiply(this.left.simplify(), this.right.simplify());
};

Multiply.prototype.diff = function(v) {
	return new Add(new Multiply(this.left, this.right.diff(v)), new Multiply(this.left.diff(v), this.right));
};

/////////////////////
function Divide(left, right) {
	BinaryOperator.call(this, left, right);
	this.operator = '/';
}

Divide.prototype = Object.create(BinaryOperator.prototype);
Divide.prototype.constructor = Divide;

Divide.prototype.apply = function(a, b) {
	return a / b;
};

Divide.prototype.simplify = function() {
	var ret = BinaryOperator.prototype.simplify.call(this);
	if (this.left.value === 0) {
		return new Const(0);
	} else if (this.right.value === 1) {
		return this.left.simplify();
	} else {
		return ret;
	}
}

Divide.prototype.create = function() {
	return new Divide(this.left.simplify(), this.right.simplify());
};

Divide.prototype.diff = function(v) {
	return new Divide(
		new Subtract(
			new Multiply(this.left.diff(v), this.right), 
			new Multiply(this.left, this.right.diff(v))), 
		new Multiply(this.right, this.right));
};


//////////////////////
function UnaryOperator(operand) {
	this.operand = operand;
}

UnaryOperator.prototype.evaluate = function(x, y, z) {
	return this.apply(this.operand.evaluate(x, y, z));
};

UnaryOperator.prototype.toString = function() {
	return this.operand.toString()+ ' ' + this.operator;
};

UnaryOperator.prototype.simplify = function() {
	//console.log('lol');
	if (this.operand instanceof Const) {
		return new Const(this.evaluate(0,0,0));
	} else if (this.toString() == this.create().toString() ) {
		return  this.create();
	} else {
		return this.create().simplify();
	}
}


//////////////////////
function Negate(operand) {
	UnaryOperator.call(this, operand);
	this.operator = 'negate';
}

Negate.prototype = Object.create(UnaryOperator.prototype);
Negate.prototype.constructor = Negate;
Negate.prototype.apply = function(a) {
	return -a;
};

Negate.prototype.create = function() {
	return new Negate(this.operand.simplify());
};

Negate.prototype.diff = function(v) {
	return new Negate(this.operand.diff(v));
};


//////////////////////
function Sin(operand) {
	UnaryOperator.call(this, operand);
	this.operator = 'sin';
}

Sin.prototype = Object.create(UnaryOperator.prototype);
Sin.prototype.constructor = Sin;
Sin.prototype.apply = function(a) {
	return Math.sin(a);
};

Sin.prototype.create = function() {
	return new Sin(this.operand.simplify());
};

Sin.prototype.diff = function(v) {
	return new Multiply(new Cos(this.operand), this.operand.diff(v));
};


////
function Cos(operand) {
	UnaryOperator.call(this, operand);
	this.operator = 'cos';
}

Cos.prototype = Object.create(UnaryOperator.prototype);
Cos.prototype.constructor = Cos;
Cos.prototype.apply = function(a) {
	return Math.cos(a);
};

Cos.prototype.create = function() {
	return new Cos(this.operand.simplify());
};

Cos.prototype.diff = function(v) {
	return new Multiply(new Negate(new Sin(this.operand)), this.operand.diff(v));
};

function parse(expr) {
    var binOperators = { '+': Add,
                         '-': Subtract,
                         '*': Multiply,
                         '/': Divide 
    };
    var unOperators = { 'negate': Negate,
                        'sin': Sin,
                        'cos': Cos
    };
    var rpn = [];
    var tokens = expr.split(/\s/);
    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        if (token in binOperators) {
            var b = rpn.pop();
            var a = rpn.pop();
            rpn.push(new binOperators[token](a,b));
        } else if (token in unOperators) {
            rpn.push(new unOperators[token](rpn.pop()));
        } else if (/^x$|^y$|^z$/.test(token)) {
            rpn.push(new Variable(token));
        } else if (/^-?[0-9]+$/.test(token)) {
            rpn.push(new Const(parseInt(token)));
        }
    }
    return rpn.pop();
}

/*var op = new Subtract(new Const(2), new Const(1));*/
//console.log(parse('x y + cos').diff('x').simplify().toString());