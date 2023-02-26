package oracle.spectra.tester.assertions;

import io.restassured.response.Response;
import oracle.spectra.tester.ParameterSupport;
import oracle.spectra.tester.model.TestAsserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Asserters {
    private static final Logger logger = LoggerFactory.getLogger(Asserters.class);
    private static final String HTTP_STATUS_ASSERTABLE_CLASS = "oracle.spectra.tester.assertions.HttpStatusAssertable";
    private static final String IS_ASSERTABLE_CLASS = "oracle.spectra.tester.assertions.IsAssertable";
    private static final String LENIENT_MATCH_ASSERTABLE_CLASS = "oracle.spectra.tester.assertions.LenientMatchAssertable";
    private static Map<String, Assertable> assertables = new ConcurrentHashMap<>();

    private final ParameterSupport parameterSupport;
    private final AssertionHandler assertionHandler;

    private Asserters(ParameterSupport parameterSupport, AssertionHandler assertionHandler) {
        this.parameterSupport = parameterSupport;
        this.assertionHandler = assertionHandler;
    }

    public static Asserters getInstance(ParameterSupport parameterSupport, AssertionHandler assertionHandler) {
        return new Asserters(parameterSupport, assertionHandler);
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
            assertionHandler.success(asserter);
        } catch(Throwable t) {
            assertionHandler.fail(asserter, t);
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
