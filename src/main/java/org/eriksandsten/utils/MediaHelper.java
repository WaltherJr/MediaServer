package org.eriksandsten.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class MediaHelper {
    private static String radxaRockServerUrl;

    public MediaHelper(@Value("${radxa_rock_server_url}") String radxaRockServerUrl) {
        MediaHelper.radxaRockServerUrl = radxaRockServerUrl;
    }

    public static void startNewChromeInstance() {
        try {
            final String[] command = {"chrome.exe", "--remote-debugging-port=9222", "--autoplay-policy=no-user-gesture-required"};
            final ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.start();

        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    public static void turnOnTV() {
        try {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(radxaRockServerUrl + "/tv-on"))
                .timeout(Duration.of(2, ChronoUnit.SECONDS))
                .GET()
                .build();
        } catch (URISyntaxException e) {
        }
    }

    public static void switchHDMIChannel(int channel) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(radxaRockServerUrl + "/tv/channel"))
                    .timeout(Duration.of(2, ChronoUnit.SECONDS))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .PUT(HttpRequest.BodyPublishers.ofString("channel=" + channel, StandardCharsets.UTF_8))
                    .build();

        } catch (URISyntaxException e) {
        }
    }

    public static void turnOffTV() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(radxaRockServerUrl + "/tv-off"))
                    .timeout(Duration.of(2, ChronoUnit.SECONDS))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
        }
    }
}
