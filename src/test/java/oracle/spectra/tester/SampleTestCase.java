package oracle.spectra.tester;

import oracle.spectra.tester.junit.DynamicTestFactory;
import oracle.spectra.tester.junit.JunitAssertionHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;

public class SampleTestCase {

    private static TestRunnerFactory factory;

    @BeforeAll
    public static void setupTestRunnerFactory() {
        var config = TestEnvironmentConfiguration.builder()
                .baseUrl("http://restapi.adequateshop.com/api")
                .assertionHandler(new JunitAssertionHandler())
                .proxyScheme("http")
                .proxyHost("www-proxy-hqdc.us.oracle.com")
                .proxyPort(80)
                .build();
        factory = TestRunnerFactory.getInstance(config);
    }

    @TestFactory
    public Iterable<DynamicTest> test() throws IOException {
        var tests = DynamicTestFactory.getTestCases(factory,"testCase1.json", "testCase2.json");
        return tests;
    }
}
