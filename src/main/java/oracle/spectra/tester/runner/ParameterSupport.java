package oracle.spectra.tester.runner;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ParameterSupport {
    private static Logger logger = LoggerFactory.getLogger(ParameterSupport.class);
    private final Map<String, Object> parameters;

    ParameterSupport() {
        parameters = new HashMap<>();
    }

    void put(String propName, Object propValue) {
        parameters.put(propName, propValue);
    }

    public Object replaceParameters(String val) {
        if (val == null) return null;

        Pattern pattern = Pattern.compile("@\\{([^}]*)}");
        java.util.regex.Matcher matcher = pattern.matcher(val);
        var buffer = new StringBuilder();
        if (matcher.find()) {
            var propName = matcher.group(1);
            if (propName != null) propName = propName.strip();
            var propValue = convertParameterToString(parameters.get(propName));
            if (logger.isDebugEnabled())
                logger.debug(String.format("%s = %s", propName, propValue));

            if (val.equals(matcher.group(0))) {
                return propValue;
            } else {
                matcher.appendReplacement(buffer, propValue);
            }
        } else {
            return val;
        }
        while (matcher.find()) {
            var propName = matcher.group(1);
            if (propName != null) propName = propName.strip();
            var propValue = convertParameterToString(parameters.get(propName));
            if (logger.isDebugEnabled())
                logger.debug(String.format("%s = %s", propName, propValue));

            matcher.appendReplacement(buffer, propValue);
        }
        return buffer.toString();
    }

    private String convertParameterToString(Object propValue) {
        if (propValue instanceof Map || propValue instanceof List) {
            throw new NotImplementedException("Property class not implemented yet - " + propValue.getClass().getName());
        }

        return propValue.toString();
    }}
