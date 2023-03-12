package oracle.spectra.tester;

import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;

public class EvalScript {
    public static void main(String[] args) throws Exception {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a Nashorn script engine
        ScriptEngine engine = factory.getEngineByName("nashorn");
        var bindings = engine.createBindings();
        bindings.put("name", "Ken Mizuta");
        bindings.put("foo", new Foo());
        // evaluate JavaScript statement
        try {
            var reader = new InputStreamReader(EvalScript.class.getClassLoader().getResourceAsStream("test.js"));
            engine.eval(reader, bindings);
        } catch (final ScriptException se) { se.printStackTrace(); }
    }

    public static class Foo {
        public void bar(String val) {
            System.out.println(String.format("Good bye, %s!", val));
        }

        public void handleMap(ScriptObjectMirror o ) {
            System.out.println(o.getClass().getName());
            o.entrySet().forEach( (e) -> {
                //if (e.getValue() instanceof Object[]) {
                if ("[object Array]".equals(e.getValue().toString())) {
                    var arrObj = (Object[]) e.getValue();
                    System.out.print(String.format("\t%s = [", e.getKey()));
                    var concat = "";
                    for (Object elem : arrObj) {
                        System.out.print(String.format("%s%s", concat, elem.toString()));
                        concat = ", ";
                    }
                    System.out.println("]");
                } else {
                    System.out.println(String.format("\t%s = %s", e.getKey(), e.getValue().toString()));
                }
            });
        }
    }
}