package TripleExpression;

public class CheckedMultiply extends BinaryOperator implements TripleExpression {
    public CheckedMultiply(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    protected void check(int a, int b) throws Exception {
        if (b != 0) {
            if (b > 0) {
                if (a > Integer.MAX_VALUE / b) {
                    throw new Exception("owerflow");
                } 
                if (a < -Integer.MAX_VALUE / b) {
                    throw new Exception("underflow");
                }
            } else {
                if (a < Integer.MAX_VALUE / b) {
                    throw new Exception("owerflow");
                } 
                if (a > -Integer.MAX_VALUE / b) {
                    throw new Exception("underflow");
                }
            }
        }
    }
    protected int apply(int a, int b) throws Exception {
        check(a, b);
        return a * b;
    }
}