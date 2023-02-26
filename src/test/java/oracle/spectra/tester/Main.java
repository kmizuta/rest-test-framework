package oracle.spectra.tester;


import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.spectra.tester.runner.TestRunnerFactory;
import oracle.spectra.tester.model.TestCases;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var main = new Main();
        main.run();
    }

    public void run() throws IOException {
        var objectMapper = new ObjectMapper();
        var testCaseJson = Main.class.getClassLoader().getResourceAsStream("testCase1.json");

        //http://restapi.adequateshop.com/api/Tourist
        var factory = TestRunnerFactory.getInstance("http://restapi.adequateshop.com/api");
        var testCases = objectMapper.readValue(testCaseJson, TestCases.class);
        System.out.println(String.format("# of test cases = %d", testCases.getTestCases().size()));
        var runner = factory.createRunner();
        testCases.getTestCases().forEach( testCase -> runner.runTest(testCase));

    }

}