package TripleExpression;

public class ExpressionParser implements Parser {
    private int index;
    private String expression;
    private double constant;
    private char variable;
    private enum State {number, plus, minus, asterisk, slash, lparen, rparen, variable}
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

    private void getNext() {
        skipWhitespace();
        char ch = getNextChar();
        if (Character.isDigit(ch)) {
            StringBuilder str = new StringBuilder();
            while (Character.isDigit(ch) || ch == '.') {
                str.append(ch);
                ch = getNextChar();
            }
            index--;
            constant = Double.parseDouble(str.toString());
            current = State.number;
            skipWhitespace();
        } else if (ch == '+') {
            current = State.plus;
            skipWhitespace();
        } else if (ch == '-') {
            current = State.minus;
            skipWhitespace();
        } else if (ch == '*') {
            current = State.asterisk;
            skipWhitespace();
        } else if (ch == '/') {
            current = State.slash;
            skipWhitespace();
        } else if (ch == '(') {
            current = State.lparen;
            skipWhitespace();
        } else if (ch == ')') {
            current = State.rparen;
            skipWhitespace();
        } else if (ch == 'x' || ch == 'y' || ch == 'z') {
            current = State.variable;
            variable = ch;
            skipWhitespace();
        }
    }

    private TripleExpression atomic() {
        getNext();
        TripleExpression ret;
        System.out.println(current);
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
                ret = new Subtract(new Const(0), atomic());
                getNext();
            break;

            case lparen:
                ret = addSubt();
                if (current != State.rparen) {
                    System.out.println(") missing");
                    System.exit(0);
                }
                getNext();
            break;

            default:
                ret = null;
                System.out.println("Unrecognizable format");
                System.exit(0);
            break;
        }
        return ret;
    }

    private TripleExpression mulDiv() {
        TripleExpression left = atomic();
        while(true) {
            switch(current) {
                case asterisk:
                    left = new Multiply(left, atomic());
                break;

                case slash:
                    left = new Divide(left, atomic());
                break;

                default:
                    return left;
            }
        }
    }

    private TripleExpression addSubt() {
        TripleExpression left = mulDiv();
        while (true) {
            switch(current) {
                case minus:
                    left = new Subtract(left, mulDiv());
                break;

                case plus:
                    left = new Add(left, mulDiv());
                break;

                default:
                    return left;
            }
        }
    }

    public TripleExpression parse(String expression) {
        this.expression = expression;
        return addSubt();
    }
}