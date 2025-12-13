package org.eriksandsten.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Locale;
import java.util.Map;

public class ThymeleafHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String createMessageStringObject(Map<String, String> messageStrings) throws JsonProcessingException {
        return objectMapper.writeValueAsString(messageStrings);
    }

    public static String getLocale() {
        return Locale.getDefault().toLanguageTag();
    }

    public static String getBaseJS() {
        return getBaseJS("[[${T(org.eriksandsten.utils.ThymeleafHelper).createMessageStringObject(messageStrings)}]]");
    }

    public static String getBaseJS(String messageStringsArgument) {
        return """

setMessageStrings(%s);
        """.formatted(messageStringsArgument);
    }
}
