package test;

import static expression.Util.list;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ExpressionTest extends BaseTest {
    protected ExpressionTest(final boolean hard) {
        super("expression.js", hard, "");

        unary.addAll(list(
                op2("negate", "negate", a -> -a)
        ));

        binary.addAll(list(
                op2("add", "+", (a, b) -> a + b),
                op2("subtract", "-", (a, b) -> a - b),
                op2("multiply", "*", (a, b) -> a * b),
                op2("divide", "/", (a, b) -> a / b)
        ));

        tests.addAll(list(
                op2("cnst(10)", "10", (x, y, z) -> 10.0),
                op2("variable('x')", "x", (x, y, z) -> x),
                op2("variable('y')", "y", (x, y, z) -> y),
                op2("variable('z')", "z", (x, y, z) -> z),
                op2("add(variable('x'), cnst(2))", "x 2 +", (x, y, z) -> x + 2),
                op2("subtract(cnst(2), variable('y'))", "2 y -", (x, y, z) -> 2 - y),
                op2("multiply(cnst(3), variable('z'))", "3 z *", (x, y, z) -> 3 * z),
                op2("divide(cnst(3), variable('z'))", "3 z /", (x, y, z) -> 3 / z),
                op2("divide(negate(variable('x')), cnst(2))", "x negate 2 /", (x, y, z) -> -x / 2),
                op2("divide(variable('x'), multiply(variable('y'), variable('z')))", "x y z * /", (x, y, z) -> x / (y * z))
        ));
    }

    public static void main(final String... args) {
        new ExpressionTest(mode(args, ExpressionTest.class, "easy", "hard") == 1).test();
    }
}
