package oracle.spectra.tester.runner.assertions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import oracle.spectra.tester.model.TestAsserter;
import oracle.spectra.tester.runner.JsonUtil;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsAssertable implements Assertable {
    private static Logger logger = LoggerFactory.getLogger(IsAssertable.class);

    @Override
    public void doAssert(Asserters asserters, TestAsserter asserter, Response response) {
        var validatableResponse = response.then();
        var parameterSupport = asserters.getParameterSupport();

        var arg = asserter.getArg();
        if (! (arg instanceof ObjectNode)) {
            logger.error("Invalid args for 'is*' assertion type");
            return;
        }

        var path = JsonUtil.getStringValue((ObjectNode)arg, "path");
        if (path == null) {
            logger.error("Missing path for assertion");
            return;
        }

        var valueNode = arg.get("value");
        if (valueNode == null) {
            logger.error("Missing value for assertion");
            return;
        }

        String value = parameterSupport.replaceParameters(valueNode.asText()).toString();
        var matcher = switch(asserter.getType()) {
            case "isString" -> Matchers.is(value);
            case "isInt" -> Matchers.is(Integer.valueOf(value));
            case "isDouble" -> Matchers.is(Double.valueOf(value));
            default -> null; // This will never happen
        };

        if (matcher == null) {
            logger.error("Invalid 'is*' matcher type.");
            return;
        }

        validatableResponse.body(path, matcher);
    }
}
