package oracle.spectra.tester.assertions;

import io.restassured.response.Response;
import oracle.spectra.tester.model.TestAsserter;

public interface Assertable {

    void doAssert(Asserters asserters, TestAsserter asserter, Response response);

}
