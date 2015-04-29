package test;

import java.util.function.DoubleUnaryOperator;

import static expression.Util.list;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ModifiedObjectExpressionTest extends ObjectExpressionTest {
    private final Op2<DoubleUnaryOperator> cos = op2("new Cos", "cos", StrictMath::cos);
    private final Op2<DoubleUnaryOperator> sin = op2("new Sin", "sin", StrictMath::sin);

    protected ModifiedObjectExpressionTest(final boolean hard, final boolean bonus) {
        super(hard, bonus);
        unary.addAll(list(cos, sin));
        tests.addAll(list(
                op3(sin(sub(vx, vy)), (x, y, z) -> StrictMath.sin(x - y), 9, 14, 1),
                op3(cos(add(vx, vy)), (x, y, z) -> StrictMath.cos(x + y), 16, 16, 1),
                op3(cos(div(sin(vz),add(vx, vy))), (x, y, z) -> StrictMath.cos(StrictMath.sin(z) / (x + y)), 55, 55, 56)
        ));
    }
    
    protected Test cos(final Test a) { return unary(cos, a); }
    protected Test sin(final Test a) { return unary(sin, a); }

    public static void main(final String[] args) {
        final int mode = mode(args, ModifiedObjectExpressionTest.class, "easy", "hard", "bonus");
        new ModifiedObjectExpressionTest(mode >= 1, mode >= 2).test();
    }
}