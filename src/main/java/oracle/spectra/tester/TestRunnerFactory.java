package oracle.spectra.tester;

import oracle.spectra.tester.assertions.AssertionHandler;

public class TestRunnerFactory {

    public static TestRunnerFactory getInstance(TestEnvironmentConfiguration config) {
        return new TestRunnerFactory(config);
    }

    private final TestEnvironmentConfiguration config;


    private TestRunnerFactory(TestEnvironmentConfiguration config) {
        this.config = config;
    }

    public TestRunner createRunner() {
        return new TestRunner(config);
    }

}
