#Easy
1. Разработайте классы `Const`, `Variable`, `Add`, `Subtract`, `Multiply`, `Divide` для представлеия выражений с одной переменной.
2. Пример описания выражения `2x-3`:
        
        var expr = new Subtract(
            new Multiply(
                new Const(2),
                new Variable("x")
            ),
            new Const(3)
        );
                                
3. Метод `evaluate(x)` должен производить вычисления вида: При вычислении такого выражения вместо каждой переменной подставляется значение `x`, переданное в качестве параметра функции `evaluate` (на данном этапе имена переменных игнорируются). Таким образом, результатом вычисления приведенного примера должно стать число `7`.
4. Метод `toString()` должен выдавать запись выражения в обратной польской записи. Например, `expr.toString()` должен выдавать `2 x * 3 -`.

#Hard
1. Метод `diff("x")` должен возвращать выражение, представляющее производную исходного выражения по переменной `x`. Например, `expr.diff("x")` должен возвращать выражение, эквивалентное `new Const(2)` (выражения 

        new Subtract(
            new Const(2), 
            new Const(0)
        )
 и

        new Subtract(
            new Add(
                new Multiply(
                    new Const(0), 
                    new Variable("x")
                ),
                new Multiply(
                    new Const(2), 
                    new Const(1)
                )
           )
           new Const(0)
        )
                             
так же будут считаться правильным ответом )

#Bonus
1. Требуется написать метод `simplify()`, производящий вычисления константных выражений. Например, `parse("x x 2 - * 1 *").diff("x").simplify().toString()`
должно возвращать `x x 2 - +`.

#Modification
1. Дополнительное реализовать поддержку:
	* унарных операций:
		* `sin` — синус, `4846147 sin` примерно равно `1`;
		* `cos` — косинус, `5419351 cos` примерно равно `1`;