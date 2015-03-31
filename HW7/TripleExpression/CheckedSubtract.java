package TripleExpression;

public class CheckedSubtract extends BinaryOperator implements TripleExpression {
    public CheckedSubtract(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    protected void check(int a, int b) throws Exception {
        if(b > 0 && a < Integer.MIN_VALUE + b) {
            throw new Exception("under");
        }
        if(b < 0 && a > Integer.MAX_VALUE + b) {
            throw new Exception("owerflow");
        } 
    }

    protected int apply(int a, int b) throws Exception {
        check(a, b);
        return a - b;
    }
}