public class Multiply extends BinaryOperator implements Expression {
    public Multiply(Expression first, Expression second) {
        fOp = first;
        sOp = second;
    }

    public double evaluate(double param) {
        rFOp = fOp.evaluate(param);
        rSOp = sOp.evaluate(param);

        return rFOp * rSOp;
    }
}