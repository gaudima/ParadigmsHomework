package TripleExpression;

abstract public class BinaryOperator implements TripleExpression {
    protected TripleExpression fOp;
    protected TripleExpression sOp;
    
    abstract public double evaluate(double x, double y, double z);
}