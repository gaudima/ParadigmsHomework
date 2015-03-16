public class Subtract extends BinaryOperator implements Expression {
    public Subtract(Expression first, Expression second) {
        fOp = first;
        sOp = second;
    }

    public double evaluate(double param) {
        rFOp = fOp.evaluate(param);
        rSOp = sOp.evaluate(param);

        return rFOp - rSOp;
    }
}