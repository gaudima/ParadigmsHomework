package TripleExpression;

public class CheckedParser implements Parser {
    private int index;
    private String expression;
    private int constant;
    private char variable;
    private enum State {number, plus, minus, asterisk, mod, slash, shiftLeft, shiftRight, square, abs, lparen, rparen, variable}
    private State current;

    private char getNextChar() {
        if (index < expression.length()) {
            char ret =  expression.charAt(index);
            index++;
            return ret;
        } else {
            return '#';
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(getNextChar())) {

        }
        index--;
    }

    private void getNext() throws Exception {
        skipWhitespace();
        char ch = getNextChar();
        if (Character.isDigit(ch)) {
            StringBuilder str = new StringBuilder();
            while (Character.isDigit(ch)) {
                str.append(ch);
                ch = getNextChar();
            }
            index--;
            try {
                constant = Integer.parseUnsignedInt(str.toString());
            } catch (NumberFormatException e) {
                throw new Exception("overflow");
            }
            current = State.number;
        } else if (ch == '+') {
            current = State.plus;
        } else if (ch == '-') {
            current = State.minus;
        } else if (ch == '*') {
            current = State.asterisk;
        } else if (ch == '/') {
            current = State.slash;
        } else if (ch == '(') {
            current = State.lparen;
        } else if (ch == ')') {
            current = State.rparen;
        } else if (ch == 'x' || ch == 'y' || ch == 'z') {
            current = State.variable;
            variable = ch;
        } else if(index < expression.length()) {
            if (expression.substring(index - 1, index + 2).equals("mod")) {
                current = State.mod;
                index += 2;
            } else if (expression.substring(index - 1, index + 1).equals("<<")) {
                current = State.shiftLeft;
                index += 1;
            } else if (expression.substring(index - 1, index + 1).equals(">>")) {
                current = State.shiftRight;
                index += 1;
            } else if (expression.substring(index - 1, index + 2).equals("abs")) {
                current = State.abs;
                index += 2;
            } else if (expression.substring(index - 1, index + 5).equals("square")) {
                current = State.square;
                index += 5;
            }
        } else {
            throw new Exception("cant parse expression");
        }
        skipWhitespace();

    }

    private TripleExpression atomic() throws Exception {
        getNext();
        TripleExpression ret;
        switch (current) {
            case number:
                ret = new Const(constant);
                getNext();
            break;

            case variable:
                ret = new Variable(Character.toString(variable));
                getNext();
            break;

            case minus:
                ret = new CheckedNegate(atomic());
            break;

            /*case abs:
                ret = new Abs(atomic());
            break;

            case square:
                ret = new Square(atomic());
            break;*/

            case lparen:
                ret = addSubt();
                if (current != State.rparen) {
                    throw new Exception(") missing");
                }
                getNext();
            break;

            default:
                ret = null;
                throw new Exception("Unrecognizable format");
        }
        return ret;
    }

    private TripleExpression mulDiv() throws Exception {
        TripleExpression left = atomic();
        while(true) {
            switch(current) {
                case asterisk:
                    left = new CheckedMultiply(left, atomic());
                break;

                case slash:
                    left = new CheckedDivide(left, atomic());
                break;

/*                case mod:
                    left = new Mod(left, atomic());
                break;*/

                default:
                    return left;
            }
        }
    }

    private TripleExpression addSubt() throws Exception {
        TripleExpression left = mulDiv();
        while (true) {
            switch(current) {
                case minus:
                    left = new CheckedSubtract(left, mulDiv());
                break;

                case plus:
                    left = new CheckedAdd(left, mulDiv());
                break;

                default:
                    return left;
            }
        }
    }

/*    private TripleExpression shifts() {
        TripleExpression left = addSubt();
        while (true) {
            switch(current) {
                case shiftLeft:
                    left = new ShiftLeft(left, addSubt());
                break;

                case shiftRight:
                    left = new ShiftRight(left, addSubt());
                break;

                default:
                    return left;
            }
        }
    }*/

    public TripleExpression parse(String expression) throws Exception {
        this.expression = expression;
        return addSubt();
    }
}