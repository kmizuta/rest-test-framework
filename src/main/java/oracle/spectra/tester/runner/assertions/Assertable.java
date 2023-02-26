package oracle.spectra.tester.runner.assertions;

import io.restassured.response.Response;
import oracle.spectra.tester.model.TestAsserter;

public interface Assertable {

    void doAssert(Asserters asserters, TestAsserter asserter, Response response);

}
