package oracle.spectra.tester.assertions;

import oracle.spectra.tester.model.TestAsserter;

public interface AssertionHandler {


    void success(TestAsserter asserter);

    void fail(TestAsserter asserter, Throwable t);
}
