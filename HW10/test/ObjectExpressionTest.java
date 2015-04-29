package test;

import javax.script.ScriptException;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static expression.Util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectExpressionTest extends BaseTest {
    public final double D = 1e-4;

    protected final Test vx = variable("x", 0);
    protected final Test vy = variable("y", 0);
    protected final Test vz = variable("z", 0);

    protected final Op2<DoubleBinaryOperator> add = op2("new Add", "+", (a, b) -> a + b);
    protected final Op2<DoubleBinaryOperator> multiply = op2("new Multiply", "*", (a, b) -> a * b);
    protected final Op2<DoubleBinaryOperator> subtract = op2("new Subtract", "-", (a, b) -> a - b);
    protected final Op2<DoubleBinaryOperator> divide = op2("new Divide", "/", (a, b) -> a / b);
    protected final Op2<DoubleUnaryOperator> neg = op2("new Negate", "negate", a -> -a);
    protected final boolean bonus;
    protected String toStringMethod = "toString";

    protected ObjectExpressionTest(final boolean hard, final boolean bonus) {
        super("objectExpression.js", hard, ".evaluate");
        this.bonus = bonus;

        unary.addAll(list(neg));
        binary.addAll(list(add, subtract, multiply, divide));

        tests.addAll(list(
                op3(cnst(10), (x, y, z) -> 10.0, 1, 1, 1),
                op3(vx, (x, y, z) -> x, 1, 1, 1),
                op3(vy, (x, y, z) -> y, 1, 1, 1),
                op3(vz, (x, y, z) -> z, 1, 1, 1),
                op3(add(vx, cnst(2)), (x, y, z) -> x + 2, 1, 1, 1),
                op3(sub(cnst(3), vy), (x, y, z) -> 3 - y, 1, 2, 1),
                op3(mul(cnst(4), vz), (x, y, z) -> 4 * z, 1, 1, 1),
                op3(div(cnst(5), vz), (x, y, z) -> 5 / z, 1, 1, 10),
                op3(div(neg(vx), cnst(2)), (x, y, z) -> -x / 2, 4, 1, 1),
                op3(div(vx, mul(vy, vz)), (x, y, z) -> x / (y * z), 21, 28, 28),
                op3(add(add(mul(vx, vx), mul(vy, vy)), mul(vz, vz)), (x, y, z) -> x * x + y * y + z * z, 5, 5, 5),
                op3(sub(add(mul(vx, vx), mul(cnst(5), mul(vz, mul(vz, vz)))), mul(vy, cnst(8))), (x, y, z) -> x * x + 5 * z * z * z - 8 * y, 5, 2, 21)
        ));
    }

    protected Test neg(final Test a) { return unary(neg, a); }

    protected Test add(final Test a, final Test b) { return binary(add, a, b); }
    protected Test sub(final Test a, final Test b) { return binary(subtract, a, b); }
    protected Test mul(final Test a, final Test b) { return binary(multiply, a, b); }
    protected Test div(final Test a, final Test b) { return binary(divide, a, b); }

    @Override
    protected void test() {
        super.test();

        if (hard) {
            for (final Op2 t : tests) {
                final Op3 test = (Op3) t;

                testDiff(test, test.name, false);
                testDiff(test, parseMethod + "('" + test.polish + "')", false);
                if (bonus) {
                    testDiff(test, parseMethod + "('" + test.polish + "')", true);
                }
            }
        }
    }

    private void testDiff(final Op3 test, final String expression, final boolean simplify) {
        for (int variable = 0; variable < 3; variable++) {
            final String s = expression + ".diff('" + "xyz".charAt(variable) + "')";
            final String value = s + (simplify ? ".simplify()" : "");
            System.out.println("Testing: " + value);
            try {
                engine.eval("expr = " + value);
                if (simplify) {
                    final int length = (int) engine.eval("expr." + toStringMethod + "().length");
                    final int expected = test.simplified[variable];
                    assertTrue(value + "." + toStringMethod + "().length too long: " + length + " instead of " + expected, length <= expected);
                }
            } catch (final ScriptException e) {
                throw new AssertionError("Script error", e);
            }
            for (int i = 1; i <= N; i += 1) {
                final double di = variable == 0 ? D : 0;
                for (int j = 1; j <= N; j += 1) {
                    final double dj = variable == 1 ? D : 0;
                    for (int k = 1; k <= N; k += 1) {
                        final double dk = variable == 2 ? D : 0;
                        final double expected = (test.f.evaluate(i + di, j + dj, k + dk) - test.f.evaluate(i - di, j - dj, k - dk)) / D / 2;
                        test(value, new double[]{i, j, k}, "expr", expected, 1e-5);
                    }
                }
            }
        }
    }

    @Override
    protected String variable(final String name) {
        return "new Variable('" + name + "')";
    }

    @Override
    protected String constant(final int value) {
        return "new Const(" + value + ")";
    }

    @Override
    protected void test(final String expression, final String polish) {
        testToString(expression, polish);

        testToString(addSpaces(expression), polish);
    }

    private void testToString(final String expression, final String expected) {
        final String script = expression + "." + toStringMethod + "()";
        try {
            assertEquals(script, engine.eval(script), expected);
        } catch (final ScriptException e) {
            throw new AssertionError("Error parsing " + script + "\n" + e.getMessage() + "\n", e);
        }
    }

    public static Op3 op3(final Test test, final TExpression f, final int sx, final int sy, final int sz) {
        return new Op3(test.expr, test.polish, f, sx, sy, sz);
    }

    public static class Op3 extends Op2<TExpression> {
        public final int[] simplified;

        public Op3(final String name, final String polish, final TExpression f, final int... simplified) {
            super(name, polish, f);
            this.simplified = simplified;
        }
    }

    public static void main(final String... args) {
        final int mode = mode(args, ObjectExpressionTest.class, "easy", "hard", "bonus");
        new ObjectExpressionTest(mode >= 1, mode >= 2).test();
    }
}
