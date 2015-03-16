public class Add extends BinaryOperator implements Expression {
    public Add(Expression first, Expression second) {
        fOp = first;
        sOp = second;
    }

    public double evaluate(double param) {
        rFOp = fOp.evaluate(param);
        rSOp = sOp.evaluate(param);

        return rFOp + rSOp;
    }
}