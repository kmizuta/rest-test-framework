package oracle.spectra.tester.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRequest {
    private String accept;
    private ObjectNode auth;
    private ObjectNode body;
    private String method;
    private String urlPath;
}
