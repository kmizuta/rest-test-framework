package oracle.spectra.tester;

import oracle.spectra.tester.model.TestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class TestFactory {

    private static final Logger logger = LoggerFactory.getLogger(TestFactory.class);

    public static TestCases getInstance(InputStream jsonInputStream) throws IOException {
        var objectMapper = JsonUtil.getObjectMapper();
        var testCases = objectMapper.readValue(jsonInputStream, TestCases.class);
        return testCases;
    }
}
