package test;

import static expression.Util.list;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ModifiedExpressionTest extends ExpressionTest {
    protected ModifiedExpressionTest(final boolean hard) {
        super(hard);
        binary.addAll(list(
                op2("mod", "%", (a, b) -> a % (double) b),
                op2("power", "**", StrictMath::pow)
        ));
        unary.addAll(list(
                op2("abs", "abs", StrictMath::abs),
                op2("log", "log", StrictMath::log)
        ));
        tests.addAll(list(
                op2("abs(subtract(variable('x'), variable('y')))", "x y - abs", (x, y, z) -> StrictMath.abs(x - y)),
                op2("log(add(variable('x'), variable('y')))", "x y + log", (x, y, z) -> StrictMath.log(x + y)),
                op2("mod(variable('x'), variable('y'))", "x y %", (x, y, z) -> x % y),
                op2("power(variable('x'), variable('y'))", "x y **", (x, y, z) -> StrictMath.pow(x, y))
        ));
    }

    public static void main(final String[] args) {
        new ModifiedExpressionTest(mode(args, ModifiedExpressionTest.class, "easy", "hard") == 1).test();
    }
}
