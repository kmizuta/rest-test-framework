package oracle.spectra.tester;


import oracle.spectra.tester.model.TestAsserter;
import oracle.spectra.tester.assertions.AssertionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        var main = new Main();
        main.run();
    }

    public void run() throws IOException {

        //http://restapi.adequateshop.com/api/Tourist
        var factory = TestRunnerFactory.getInstance("http://restapi.adequateshop.com/api", new SampleAssertionHandler());
        var testCases = TestFactory.getInstance(Main.class.getClassLoader().getResourceAsStream("testCase1.json"));

        System.out.printf("# of test cases = %d%n", testCases.getTestCases().size());
        var runner = factory.createRunner();
        testCases.getTestCases().forEach( testCase -> runner.runTest(testCase));

    }

    class SampleAssertionHandler implements AssertionHandler {
        @Override
        public void success(TestAsserter asserter) {
            logger.info("Assertion Success - " + JsonUtil.objectAsJsonString(asserter));
        }

        @Override
        public void fail(TestAsserter asserter, Throwable t) {
            logger.error("Assertion failed - " + JsonUtil.objectAsJsonString(asserter), t);
        }
    }

}