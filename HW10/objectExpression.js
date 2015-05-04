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
//println(new Divide(new Const(5), new Variable('z')).diff('x'));