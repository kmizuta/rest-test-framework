package oracle.spectra.tester.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.RequestSpecification;
import oracle.spectra.tester.model.TestCase;
import oracle.spectra.tester.model.TestRequest;
import oracle.spectra.tester.model.TestResponse;
import oracle.spectra.tester.runner.assertions.Asserters;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class TestRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    private final String baseUrl;
    private final ParameterSupport parameterSupport;
    private final Asserters asserters;

    TestRunner(String baseUrl) {
        this.baseUrl = baseUrl;
        this.parameterSupport = new ParameterSupport();
        this.asserters = Asserters.getInstance(parameterSupport);
    }

    public void runTest(TestCase testCase) {
        logger.info(String.format("Running test - %s", testCase.getName()));

        var requestSpecification = given(testCase.getRequest());
        var method = testCase.getRequest().getMethod();
        var url = getUrl(testCase.getRequest());

        if (logger.isDebugEnabled()) {
            logger.debug(url.toString());
        }
        var response =
            switch(method) {
                case "GET" -> requestSpecification.get(url);
                case "POST" -> requestSpecification.post(url);
                case "PUT" -> requestSpecification.put(url);
                case "PATCH" -> requestSpecification.patch(url);
                default -> null;
            };
        if (response == null) {
            throw new RuntimeException("Invalid HTTP method");
        }

        assertResponse(testCase.getResponse(), response);
    }

    private URL getUrl(TestRequest request) {
        var builder = new StringBuilder();
        var urlPath = parameterSupport.replaceParameters(request.getUrlPath());
        var url = builder.append(baseUrl).append("/").append(urlPath).toString();
        logger.info(url);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    private Response assertResponse(TestResponse expectedResponse, Response response) {
        logger.debug(response.asPrettyString());

        var assertions = expectedResponse.getAssertions();
        if (assertions != null) {
            assertions.forEach( asserter -> asserters.doAssert(asserter, response) );
        }

        var set = expectedResponse.getSet();
        if (set != null) {
            final var jsonPath = response.jsonPath();
            set.forEach( (propName, jsonPathExpression) -> {
                var propValue = jsonPath.get(jsonPathExpression);
                parameterSupport.put(propName, propValue);
            });
        }

        return response;
    }

    private Matcher getMatcher(JsonNode matcherNode) {
        if (! (matcherNode instanceof ObjectNode))
            throw new RuntimeException("Invalid \"matcher\" node");

        var matcherObject = (ObjectNode) matcherNode;
        var isNode = matcherObject.get("is");
        if (isNode != null) {
            logger.info(isNode.getNodeType().toString());
            if (isNode != null) {
                switch (isNode.getNodeType()) {
                    case STRING:
                        var value = parameterSupport.replaceParameters(isNode.asText());
                        if (value instanceof Integer)
                            return Matchers.is((Integer)value);
                        if (value instanceof Double)
                            return Matchers.is((Double)value);
                        return Matchers.is(value.toString());
                    case NUMBER:
                        var s = isNode.asText();
                        if (s.contains("."))
                            return Matchers.is(isNode.asDouble());
                        else
                            return Matchers.is(isNode.asInt());
                }
            }
        }

        return null;
    }


    private RequestSpecification given(TestRequest request) {
        var given = RestAssured.given();

        if (request.getAccept() != null) given.accept(request.getAccept());
        if (request.getAuth() != null) auth(given.auth(), request.getAuth());
        if (request.getBody() != null) body(given, request.getBody());

        return given;
    }

    private AuthenticationSpecification auth(AuthenticationSpecification authSpec, ObjectNode authNode) {
        var oauth2Token = JsonUtil.getStringValue(authNode, "oauth2");
        if (oauth2Token != null)
            authSpec.oauth2(oauth2Token);

        return authSpec;
    }

    private RequestSpecification body(RequestSpecification given, ObjectNode bodyNode) {
        var mapper = new ObjectMapper();
        try {
            var bodyJson = mapper.writeValueAsString(bodyNode);
            System.out.println(bodyJson);
            given.body(bodyJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return given;
    }




}
