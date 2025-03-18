package org.eriksandsten.utils;

import java.io.IOException;

public final class MediaServerUtils {
    public static String readResourceFile(String filename) {
        try (var youtubeJSFileStream = MediaServerUtils.class.getClassLoader().getResource(filename).openStream()) {
            return new String(youtubeJSFileStream.readAllBytes());
        } catch (IOException e) {
            return null;
        }
    }
}
