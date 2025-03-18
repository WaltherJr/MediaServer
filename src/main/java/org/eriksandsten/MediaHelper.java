package org.eriksandsten;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
                    .header("Content-Type", "application/x-www-form-urlencoded") // Set content type
                    .PUT(HttpRequest.BodyPublishers.ofString("channel=" + channel, StandardCharsets.UTF_8))
                    .build();

        } catch (URISyntaxException e) {
        }
    }

    public static void turnOffTV() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(radxaRockServerUrl + "/tv-ff"))
                    .timeout(Duration.of(2, ChronoUnit.SECONDS))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
        }
    }
}
