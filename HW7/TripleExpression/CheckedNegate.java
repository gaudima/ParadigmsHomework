package TripleExpression;

public class CheckedNegate implements TripleExpression {
	private TripleExpression value;
    public CheckedNegate(TripleExpression val) {
        value = val;
    }

    private void check(int a) throws Exception {
        if (a < Integer.MIN_VALUE) {
            throw new Exception("underflow");
        }
        if (a > Integer.MAX_VALUE) {
            throw new Exception("overflow");
        }
    }

    public int evaluate(int x, int y, int z) throws Exception {
        int a = value.evaluate(x, y, z);
        check(a);
        return -a;
    }
}