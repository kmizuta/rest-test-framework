package oracle.spectra.tester;

import io.restassured.response.Response;
import oracle.spectra.tester.assertions.Assertable;
import oracle.spectra.tester.assertions.Asserters;
import oracle.spectra.tester.model.TestAsserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleAsserter implements Assertable {

    private static final Logger logger = LoggerFactory.getLogger(SampleAsserter.class);
    @Override
    public void doAssert(Asserters asserters, TestAsserter asserter, Response response) {
        logger.info("Sample Asserter invoked!!");
    }
}
