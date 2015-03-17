package TripleExpression;

public class Divide extends BinaryOperator implements TripleExpression {
    public Divide(TripleExpression first, TripleExpression second) {
        fOp = first;
        sOp = second;
    }

    public double evaluate(double x, double y, double z) {
        return fOp.evaluate(x, y, z) / sOp.evaluate(x, y, z);
    }
}