package oracle.spectra.tester.junit;

import oracle.spectra.tester.JsonUtil;
import oracle.spectra.tester.assertions.AssertionHandler;
import oracle.spectra.tester.model.TestAsserter;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class JunitAssertionHandler implements AssertionHandler {
    private static final Logger logger = LoggerFactory.getLogger(JunitAssertionHandler.class);

    @Override
    public void success(TestAsserter asserter) {
        logger.info("Assertion Success - " + JsonUtil.objectAsJsonString(asserter));
        assertTrue(true, JsonUtil.objectAsJsonString(asserter));
    }

    @Override
    public void fail(TestAsserter asserter, Throwable t) {
        logger.error("Assertion failed - " + JsonUtil.objectAsJsonString(asserter), t);
        Assertions.fail(JsonUtil.objectAsJsonString(asserter) + "\n" + t.getMessage());
    }
}
