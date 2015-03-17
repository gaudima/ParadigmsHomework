package TripleExpression;

public class Add extends BinaryOperator implements TripleExpression {
    public Add(TripleExpression first, TripleExpression second) {
        fOp = first;
        sOp = second;
    }

    public double evaluate(double x, double y , double z) {
        return fOp.evaluate(x ,y ,z) + sOp.evaluate(x, y, z);
    }
}