package test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class JSEngine {
    public static class IO {
        private final ScriptEngine engine;
        public IO(final ScriptEngine engine) {
            this.engine = engine;
        }

        public void print(final String message) {
            System.out.print(message);
        }

        public void println(final String message) {
            System.out.println(message);
        }

        public void include(final String file) throws Exception {
            engine.eval(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        }
    }

    public static ScriptEngine createEngine() throws ScriptException {
        final ScriptEngineManager factory = new ScriptEngineManager();
        final ScriptEngine engine = factory.getEngineByName("JavaScript");
        engine.put("io", new IO(engine));
        engine.eval("var println = function() { io.println(Array.prototype.map.call(arguments, String).join(' ')); };");
        engine.eval("var print   = function() { io.print  (Array.prototype.map.call(arguments, String).join(' ')); };");
        engine.eval("var include = function(file) { io.include(file); }");
        return engine;
    }
}
