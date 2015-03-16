abstract public class BinaryOperator implements Expression {
    protected Expression fOp;
    protected Expression sOp;
    protected double rFOp;
    protected double rSOp;
    
    abstract public double evaluate(double param);

}