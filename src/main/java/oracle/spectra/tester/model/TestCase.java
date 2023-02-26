package oracle.spectra.tester.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {

    private String name;
    private TestRequest request;
    private TestResponse response;
}
