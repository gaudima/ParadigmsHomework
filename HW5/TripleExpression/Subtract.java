package TripleExpression;

public class Subtract extends BinaryOperator implements TripleExpression {
    public Subtract(TripleExpression first, TripleExpression second) {
        fOp = first;
        sOp = second;
    }

    public double evaluate(double x, double y, double z) {
        return fOp.evaluate(x, y, z) - sOp.evaluate(x, y, z);
    }
}