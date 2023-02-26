package oracle.spectra.tester.runner.assertions;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import io.restassured.response.Response;
import oracle.spectra.tester.model.TestAsserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpStatusAssertable implements Assertable {
    private static Logger logger = LoggerFactory.getLogger(HttpStatusAssertable.class);

    @Override
    public void doAssert(Asserters asserters, TestAsserter asserter, Response response) {
        var validatableResponse = response.then();
        var statusCode = asserter.getArg();
        if (statusCode == null || statusCode.getNodeType() != JsonNodeType.NUMBER) {
            logger.error("Invalid status code for httpStatus asserter");
            return;
        }

        validatableResponse.statusCode(statusCode.asInt());
    }
}
