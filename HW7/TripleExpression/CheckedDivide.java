package TripleExpression;

public class CheckedDivide extends BinaryOperator implements TripleExpression {
    public CheckedDivide(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    protected void check(int a, int b) throws Exception {
        if (b == 0) {
            throw new Exception("division by zero");
        }
    }
    protected int apply(int a, int b) throws Exception {
        check(a, b);
        return a / b;
    }
}