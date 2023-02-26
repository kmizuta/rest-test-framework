package oracle.spectra.tester.runner.assertions;

import io.restassured.response.Response;
import oracle.spectra.tester.model.TestAsserter;
import oracle.spectra.tester.runner.JsonUtil;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LenientMatchAssertable implements Assertable {
    private static Logger logger = LoggerFactory.getLogger(LenientMatchAssertable.class);

    @Override
    public void doAssert(Asserters asserters, TestAsserter asserter, Response response) {
        var expected = JsonUtil.nodeAsString(asserter.getArg());
        var actual = response.getBody().asString();
        logger.debug(actual);

        try {
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
