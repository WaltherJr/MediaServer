package org.eriksandsten.utils;

import java.io.IOException;
import java.io.InputStream;

public final class MediaServerUtils {
    public static String readResourceFile(String filename) {
        try (final InputStream youtubeJSFileStream = MediaServerUtils.class.getClassLoader().getResource(filename).openStream()) {
            return new String(youtubeJSFileStream.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
    }
}
