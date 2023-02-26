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
        factory = TestRunnerFactory.getInstance("http://restapi.adequateshop.com/api", new JunitAssertionHandler());
    }

    @TestFactory
    public Iterable<DynamicTest> test() throws IOException {
        var tests = DynamicTestFactory.getTestCases(factory,"testCase1.json", "testCase2.json");
        return tests;
    }
}
