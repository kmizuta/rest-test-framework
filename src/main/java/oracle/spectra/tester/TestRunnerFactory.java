package oracle.spectra.tester;

import oracle.spectra.tester.assertions.AssertionHandler;

public class TestRunnerFactory {

    public static TestRunnerFactory getInstance(String baseUrl, AssertionHandler assertionHandler) {
        return new TestRunnerFactory(baseUrl, assertionHandler);
    }

    private final String baseUrl;
    private final AssertionHandler assertionHandler;


    private TestRunnerFactory(String baseUrl, AssertionHandler assertionHandler) {
        this.baseUrl = baseUrl;
        this.assertionHandler = assertionHandler;
    }

    public TestRunner createRunner() {
        return new TestRunner(baseUrl, assertionHandler);
    }

}
