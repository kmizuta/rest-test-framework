package oracle.spectra.tester.assertions;

import io.restassured.response.Response;
import oracle.spectra.tester.JsonUtil;
import oracle.spectra.tester.model.TestAsserter;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LenientMatchAssertable implements Assertable {
    private static final Logger logger = LoggerFactory.getLogger(LenientMatchAssertable.class);

    @Override
    public void doAssert(Asserters asserters, TestAsserter asserter, Response response) {
        var expected = JsonUtil.objectAsJsonString(asserter.getArg());
        var actual = response.getBody().asString();
        logger.debug(actual);

        try {
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
