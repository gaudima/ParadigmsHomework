function binary(left, right, apply) {
    function evaluate(x, y, z) {
        return apply(left(x, y, z), right(x, y, z));
    }
    return evaluate;
}

function add(left, right) {
    return binary(left, right, function(a, b) { return a + b; });
}

function subtract(left, right) {
    return binary(left, right, function(a, b) { return a - b; });
}

function multiply(left, right) {
    return binary(left, right, function(a, b) { return a * b; });
}

function divide(left, right) {
    return binary(left, right, function(a, b) { return a / b; });
}

function cnst(val) {
    function evaluate(x, y, z) {
        return val;
    }
    return evaluate;
}

function variable(name) {
    function evaluate(x, y, z) {
        if (name == 'x') {
            return x;
        } else if (name == 'y') {
            return y;
        } else {
            return z;
        }
    }
    return evaluate;
}

function unary(operand, apply) {
    function evaluate(x, y, z) {
        return apply(operand(x, y, z));
    }
    return evaluate;
}

function negate(operand) {
    return unary(operand, function(a) { return -a; });
}

function log(operand) {
    return unary(operand, function(a) { return Math.log(a); });
}

function abs(operand) {
    return unary(operand, function(a) { return Math.abs(a); });
}

function power(left, right) {
    return binary(left, right, function(a, b) { return Math.pow(a, b); });
}

function mod(left, right) {
    return binary(left, right, function(a, b) { return a % b; });
}

function parse(expr) {
    var binOperators = { '+': add,
                         '-': subtract,
                         '*': multiply,
                         '/': divide,
                         '**': power,
                         '%': mod 
    };
    var unOperators = { 'negate': negate,
                        'abs': abs,
                        'log': log
    }
    var rpn = [];
    var tokens = expr.split(/\s/);
    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        if (token in binOperators) {
            var b = rpn.pop();
            var a = rpn.pop();
            rpn.push(binOperators[token](a,b));
        } else if (token in unOperators) {
            rpn.push(unOperators[token](rpn.pop()));
        } else if (/^x$|^y$|^z$/.test(token)) {
            rpn.push(variable(token));
        } else if (/^-?[0-9]+$/.test(token)) {
            rpn.push(cnst(parseInt(token)));
        }
    }
    return rpn.pop();
}
//console.log(parse('10')(0));
