package oracle.spectra.tester.runner;

public class TestRunnerFactory {

    public static TestRunnerFactory getInstance(String baseUrl) {
        return new TestRunnerFactory(baseUrl);
    }

    private final String baseUrl;

    private TestRunnerFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public TestRunner createRunner() {
        return new TestRunner(baseUrl);
    }

}
