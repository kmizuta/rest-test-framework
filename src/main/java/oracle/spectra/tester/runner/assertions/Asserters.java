package oracle.spectra.tester.runner.assertions;

import io.restassured.response.Response;
import oracle.spectra.tester.runner.ParameterSupport;
import oracle.spectra.tester.model.TestAsserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Asserters {
    private static final Logger logger = LoggerFactory.getLogger(Asserters.class);
    private static final String HTTP_STATUS_ASSERTABLE_CLASS = "oracle.spectra.tester.runner.assertions.HttpStatusAssertable";
    private static final String IS_ASSERTABLE_CLASS = "oracle.spectra.tester.runner.assertions.IsAssertable";
    private static final String LENIENT_MATCH_ASSERTABLE_CLASS = "oracle.spectra.tester.runner.assertions.LenientMatchAssertable";
    private static Map<String, Assertable> assertables = new ConcurrentHashMap<>();

    private final ParameterSupport parameterSupport;

    private Asserters(ParameterSupport parameterSupport) {
        this.parameterSupport = parameterSupport;
    }

    public static Asserters getInstance(ParameterSupport parameterSupport) {
        return new Asserters(parameterSupport);
    }

    public void doAssert(TestAsserter asserter, Response response) {
        var asserterClass = switch(asserter.getType()) {
            case "httpStatus" -> HTTP_STATUS_ASSERTABLE_CLASS;
            case "isString" -> IS_ASSERTABLE_CLASS;
            case "isInt" -> IS_ASSERTABLE_CLASS;
            case "isDouble" -> IS_ASSERTABLE_CLASS;
            case "lenientMatch" -> LENIENT_MATCH_ASSERTABLE_CLASS;
            default -> asserter.getType();
        };

        Assertable assertable = getAssertableInstance(asserterClass);
        if (assertable == null) {
            logger.error("Unable to instantiate asserter class " + asserterClass);
            return;
        }
        try {
            assertable.doAssert(this, asserter, response);
        } catch(Throwable t) {
            logger.error("Assertion failed", t);
        }
    }

    private Assertable getAssertableInstance(String asserterClass) {
        return assertables.computeIfAbsent(asserterClass, className -> {
            try {
                var clazz = Class.forName(className);
                var constructor = clazz.getConstructor();
                return (Assertable)constructor.newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException | ClassCastException e) {
                logger.error("Error getting assertable instance", e);
                return null;
            }
        });
    }


    public ParameterSupport getParameterSupport() {
        return parameterSupport;
    }
}
