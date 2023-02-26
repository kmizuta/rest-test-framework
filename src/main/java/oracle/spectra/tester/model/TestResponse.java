package oracle.spectra.tester.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResponse {

    private List<TestAsserter> assertions;
    private Map<String, String> set;

}
