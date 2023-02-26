
# Declarative Test Framework for REST Services

## How to Use

Test cases are defined as JSON files. A single JSON file represents a "stateful" test suite. 
This file can contain one or more REST calls where output from one REST service can be 
used to control the behavior or assertions of future REST calls in the same JSON file.

### JSON Representation

Here is an example of a test JSON file. This sample includes two test cases that are run 
one after the other. For each, there is a series of assertions that are declared. You can also 
store content from the current payload to be used in the future.

- Test 1
  - Assertions....
    - httpStatus - asserts that the HTTP rsponse status is 200
    - isInt - asserts that $.page of the response equals 1
    - isString - asserts that $.data[1].tourist_name equals "Bala"
    - lenientMatch - asserts that the response matches leniently with the given "arg" JSON. 
      (See details on LENIENT match from JSONAssert library)
  - Set...
    - id, tourist_email, x, num_tourists all store the associated value of the JSON path expression 
- Test 2
  - isString - asserts that $.tourist_email equals  tourist_email as stored from the previous request.
  - oracle.spectra.tester.SampleAsserter - You can create custom asserters if the framework does
    not provide something you can do declaratively.

```json
{
  "testCases": [
    {
      "name": "Test 1",
      "request": {
        "method": "GET",
        "urlPath": "Tourist"
      },
      "response": {
        "assertions": [
          { "type": "httpStatus", "arg":  200 },
          { "type": "isInt", "arg":  { "path":  "page", "value":  1 } },
          { "type": "isString", "arg":  { "path":  "data[1].tourist_name", "value":  "Bala" } },
          { "type": "lenientMatch", "arg": { "page":1,"per_page":10,"totalrecord":6246,"total_pages":625}}
        ],
        "set": {
          "id": "data[1].id",
          "tourist_email": "data[1].tourist_email",
          "x": "totalrecord",
          "num_tourists": "data.size()"
        }
      }
    },
    {
      "name": "Test 2",
      "request": {
        "method": "GET",
        "urlPath": "Tourist/@{ id }"
      },
      "response": {
        "assertions": [
          { "type": "isString", "arg":  { "path":  "tourist_email", "value":  "@{ tourist_email }" } },
          { "type":  "oracle.spectra.tester.SampleAsserter" }
        ]
      }
    }
  ]
}
```

### Assertions

There are a number of assertions that is supported out of the box.

| Assertion Type | Additional Properties | Description |
|----------------|-------------|-----------|
| isString, isInt, isDouble | "arg": { "path": <JSON Path Expression>}, "value": value } | Asserts that the evaluated JSON Path expression equals the value |
| lenientMatch | "arg": <json object> | Does a JSON Assert lenient match of the response body |

The above are simply shorthand for common, prebuilt assertions. You can also define additional asserters by implementing
`oracle.spectra.tester.assertions.Assertable`. Your custom Assertables can take whatever argument
you want. You will be passed a `JsonNode` representing the additional properties through
the `TestAsserter` classs. You will also be given a `Response` object as well as 
a `Asserters` object (which is used to evaluate previous stored state).

### Using in JUnit

The following is an example of a JUnit test class.

```java
public class SampleTestCase {

    private static TestRunnerFactory factory;

    @BeforeAll
    public static void setupTestRunnerFactory() {
        factory = TestRunnerFactory.getInstance("http://restapi.adequateshop.com/api", new JunitAssertionHandler());
    }

    @TestFactory
    public Iterable<DynamicTest> test() throws IOException {
        return DynamicTestFactory.getTestCases(factory,"testCase1.json", "testCase2.json");
    }
}
```

The `TestRunnerFactory` is instantiated at the beginning and is used to configure the client. 
This class can be used to control the base URL for all REST services in the test case. This enables
you to control which environment the test cases are run against. 

`DynamicTestFactory` is used to convert the test JSON files into a list of DynamicTests that are
run by JUnit. 


