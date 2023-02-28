package oracle.spectra.tester;

import lombok.Builder;
import lombok.Getter;
import oracle.spectra.tester.assertions.AssertionHandler;

@Getter
@Builder
public class TestEnvironmentConfiguration {

    private String baseUrl;
    private String proxyScheme;
    private String proxyHost;
    private int proxyPort;
    private AssertionHandler assertionHandler;

}
