package test;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import static expression.Util.*;

/**
 * @author Niyaz Nigmatullin
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseTest {
    public static final int N = 5;
    public final double EPS = 1e-9;

    protected final List<Op2<DoubleUnaryOperator>> unary = new ArrayList<>();
    protected final List<Op2<DoubleBinaryOperator>> binary = new ArrayList<>();
    protected List<Op2<TExpression>> tests = new ArrayList<>();
    protected String parseMethod = "parse";

    protected final ScriptEngine engine;
    final boolean hard;
    private final String evaluate;

    protected BaseTest(final String script, final boolean hard, final String evaluate) {
        this.hard = hard;
        this.evaluate = evaluate;

        try {
            engine = JSEngine.createEngine();
            engine.eval("var expr;");
        } catch (final ScriptException e) {
            throw new AssertionError("Invalid initialization", e);
        }
        try {
            engine.eval(new InputStreamReader(new FileInputStream(script), "UTF-8"));
        } catch (final ScriptException e) {
            throw new AssertionError("Script error", e);
        } catch (final UnsupportedEncodingException e) {
            throw new AssertionError("Fail", e);
        } catch (final FileNotFoundException e) {
            throw new AssertionError("Script not found", e);
        }
    }

    protected String addSpaces(final String expression) {
        String spaced = expression;
        for (int n = StrictMath.min(10, 200 / expression.length()); n > 0;) {
            final int index = randomInt(spaced.length() + 1);
            final char c = index == 0 ? 0 : spaced.charAt(index - 1);
            final char nc = index == spaced.length() ? 0 : spaced.charAt(index);
            if ((!Character.isDigit(c) && c != '-' || !Character.isDigit(nc)) && (!Character.isLetterOrDigit(c) || !Character.isLetterOrDigit(nc)) && c != '\'' && nc != '\'') {
                spaced = spaced.substring(0, index) + " " + spaced.substring(index);
                n--;
            }
        }
        return spaced;
    }

    protected void test() {
        for (final Op2<TExpression> test : tests) {
            test(test.name, test.f, test.polish);
            if (hard) {
                test(parseMethod + "('" + test.polish + "')", test.f, test.polish);
                test(parseMethod + "('" + addSpaces(test.polish) + "')", test.f, test.polish);
            }
        }

        testRandom(500, (v, i) -> generate(v, i / 5 + 2));
        System.out.println("OK");
    }

    protected void test(final String expression, final TExpression f, final String polish) {
        System.out.println("Testing: " + expression);
        test(expression, polish);
        try {
            engine.eval("expr = " + expression);
        } catch (final ScriptException e) {
            throw new AssertionError("Script error", e);
        }
        for (double i = 1; i <= N; i += 1) {
            for (double j = 1; j <= N; j += 1) {
                for (double k = 1; k <= N; k += 1) {
                    test(expression, new double[]{i, j, k}, "expr", f.evaluate(i, j, k), EPS);
                }
            }
        }
    }

    protected void test(final String expression, final String polish) {
    }

    public void testRandom(final int n, final BiFunction<double[], Integer, Test> f) {
        System.out.println("Testing random tests");
        for (int i = 0; i < n; i++) {
            if (i % 100 == 0) {
                System.out.println("    Completed " + i + " out of " + n);
            }
            final double[] vars = new double[]{RNG.nextDouble(), RNG.nextDouble(), RNG.nextDouble()};

            final Test test = f.apply(vars, i);

            test(test.expr, vars, test.expr, test.answer, EPS);
            test(test.expr, test.polish);
            test(addSpaces(test.expr), test.polish);
            if (hard) {
                final String expr = parseMethod + "('" + test.polish + "')";
                test(expr, vars, expr, test.answer, EPS);
                test(expr, test.polish);
            }
        }
    }

    protected void test(final String context, final double[] vars, final String expression, final double expected, final double precision) {
        try {
            final Object result = engine.eval(String.format("%s%s(%.20f, %.20f, %.20f);", expression, evaluate, vars[0], vars[1],  vars[2]));
            if (result instanceof Number) {
                assertEquals(String.format("f(%.20f, %.20f, %.20f)\n%s", vars[0], vars[1], vars[2], context), precision, ((Number) result).doubleValue(), expected);
            } else {
                throw new AssertionError(String.format(
                        "Expected number, found \"%s\" (%s) for x = %.20f, y = %.20f, z = %.20f in\n%s",
                        result, result.getClass().getSimpleName(),
                        vars[0], vars[1], vars[2],
                        context
                ));
            }
        } catch (final ScriptException e) {
            throw new AssertionError(String.format("No error expected for x = %.20f, y = %.20f, z = %.20f in\n%s", vars[0], vars[1], vars[2], context), e);
        }
    }

    private Test generate(final double[] vars, final int depth) {
        if (depth == 0) {
            return constOrVariable(vars);
        }
        final int operator = randomInt(6);
        if (operator <= 0) {
            return genP(vars, depth);
        } else if (operator <= 1) {
            return unary(random(unary), genP(vars, depth));
        } else {
            return binary(random(binary), genP(vars, depth), genP(vars, depth));
        }
    }

    private Test genP(final double[] vars, final int depth) {
        return generate(vars, randomInt(depth));
    }

    private Test constOrVariable(final double[] vars) {
        if (RNG.nextBoolean()) {
            final int id = randomInt(3);
            final String name = "xyz".charAt(id) + "";
            return variable(name, vars[id]);
        } else {

            return cnst(RNG.nextInt());
        }
    }

    protected Test variable(final String name, final double value) {
        return new Test(variable(name), name, value);
    }

    protected Test cnst(final int value) {
        return new Test(constant(value), value + "", value);
    }

    protected String constant(final int value) {
        return "cnst(" + value + ")";
    }

    protected String variable(final String name) {
        return "variable('" + name + "')";
    }

    protected Test binary(final Op2<DoubleBinaryOperator> op, final Test t1, final Test t2) {
        return new Test(
                op.name + "(" + t1.expr + ", " + t2.expr + ")",
                binary(op.polish, t1.polish, t2.polish),
                op.f.applyAsDouble(t1.answer, t2.answer)
        );
    }

    protected String binary(final String o, final String arg1, final String arg2) {
        return arg1 + " " + arg2 + " " + o;
    }

    protected Test unary(final Op2<DoubleUnaryOperator> op, final Test arg) {
        return new Test(op.name + "(" + arg.expr + ")", unary(op.polish, arg.polish), op.f.applyAsDouble(arg.answer));
    }

    protected String unary(final String op, final String arg) {
        return arg + " " + op;
    }

    public static class Test {
        final String expr;
        final String polish;
        final double answer;

        Test(final String expr, final String polish, final double answer) {
            this.expr = expr;
            this.polish = polish;
            this.answer = answer;
        }
    }

    public interface TExpression {
        double evaluate(double x, double y, double z);
    }

    public static class Op2<T> {
        public final String name;
        public final String polish;
        public final T f;

        protected Op2(final String name, final String polish, final T f) {
            this.name = name;
            this.polish = polish;
            this.f = f;
        }
    }

    public static <T> Op2<T> op2(final String name, final String polish, final T f) {
        return new Op2<>(name, polish, f);
    }

    protected static int mode(final String[] args, final Class<?> type, final String... modes) {
        checkAssert(type);

        if (args.length == 0) {
            System.err.println("No arguments found");
        } else if (args.length > 1) {
            System.err.println("Only one argument expected, " + args.length + " found");
        } else if (Arrays.asList(modes).indexOf(args[0]) < 0) {
            System.err.println("First argument should be one of: \"" + String.join("\", \"", modes) + "\", found: \"" + args[0] + "\"");
        } else {
            return Arrays.asList(modes).indexOf(args[0]);
        }
        System.err.println("Usage: java -ea " + type.getName() + " (" + String.join("|", modes) + ")");
        System.exit(0);
        return -1;
    }
}
