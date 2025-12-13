package org.eriksandsten.utils;

import org.springframework.context.MessageSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class MediaServerUtils {
    public static String readResourceFile(String filename) {
        try (final InputStream youtubeJSFileStream = MediaServerUtils.class.getClassLoader().getResource(filename).openStream()) {
            return new String(youtubeJSFileStream.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public static Map<String, String> createMessageStrings(MessageSource messageSource, String... messageKeys) {
        return Arrays.stream(messageKeys).collect(Collectors.toMap(
                messageKey -> messageKey,
                messageKey -> messageSource.getMessage(messageKey, null, Locale.getDefault())
        ));
    }
}
