package oracle.spectra.tester;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String getStringValue(ObjectNode node, String propName) {
        var childNode = node.get(propName);
        return childNode == null ? null : childNode.asText();
    }

    public static String objectAsJsonString(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
