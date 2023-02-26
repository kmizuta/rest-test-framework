package oracle.spectra.tester.junit;

import oracle.spectra.tester.JsonUtil;
import oracle.spectra.tester.TestRunnerFactory;
import oracle.spectra.tester.model.TestCases;
import org.junit.jupiter.api.DynamicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DynamicTestFactory {
    private static Logger logger = LoggerFactory.getLogger(DynamicTestFactory.class);

    public static List<DynamicTest> getTestCases(TestRunnerFactory factory, InputStream... jsonInputStreams) throws IOException {
        var objectMapper = JsonUtil.getObjectMapper();
        List<DynamicTest> dynamicTests = new ArrayList<>();
        for (var jsonInputStream : jsonInputStreams) {
            var runner = factory.createRunner();
            var testCases = objectMapper.readValue(jsonInputStream, TestCases.class);
            testCases.getTestCases().forEach(testCase -> {
                dynamicTests.add(DynamicTest.dynamicTest(testCase.getName(), () -> {
                    runner.runTest(testCase);
                }));
            });
        }
        return dynamicTests;
    }

    public static List<DynamicTest> getTestCases(TestRunnerFactory factory, Class testClazz, String... jsonResourceNames) throws IOException {
        List<InputStream> jsonInputStreams = new ArrayList<>();
        for (var jsonResourceName : jsonResourceNames) {
            jsonInputStreams.add(testClazz.getClassLoader().getResourceAsStream(jsonResourceName));
        }

        return getTestCases(factory, jsonInputStreams.toArray(new InputStream[0]));
    }

    public static List<DynamicTest> getTestCases(TestRunnerFactory factory, String... jsonResourceNames) throws IOException {
        return getTestCases(factory, DynamicTestFactory.class, jsonResourceNames);
    }
}

