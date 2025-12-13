package org.eriksandsten.philips;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eriksandsten.exception.Http400BadRequestException;
import org.eriksandsten.philips.request.PhilipsTVMuteStateRequest;
import org.eriksandsten.philips.request.PhilipsTVVolumeRequest;
import org.eriksandsten.philips.request.SetTVMuteStateRequest;
import org.eriksandsten.philips.response.PowerStateResponse;
import org.eriksandsten.philips.response.PhilipsTVVolumeAndMuteStateResponse;
import org.eriksandsten.request.SetTVVolumeRequest;
import org.eriksandsten.utils.PhilipsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;

/**
 * https://github.com/eslavnov/pylips/tree/master/docs/Chapters
 */
@Service
public class PhilipsTVService<T> {
    private final String philipsTVServerUrl;
    private static final int NUMBER_OF_SOURCE_MENU_ITEMS = 7;
    private static final int NUMBER_OF_SOURCE_MENU_ITEMS_BEFORE_HDMI_ITEMS = 3;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient;
    private final Duration defaultRequestTimeoutInMs;
    private final PhilipsHelper philipsHelper;
    private final MessageSource messageSource;
    private final String TV_VOLUME_AND_MUTESTATE_ENDPOINT = "/6/audio/volume";

    @Autowired
    public PhilipsTVService(@Value("${philips_tv_server_url}") String philipsTVServerUrl,
                            @Value("${philips_tv_server_request_timeout_in_ms}") String defaultRequestTimeout,
                            PhilipsHelper philipsHelper,
                            MessageSource messageSource) {
        this.philipsTVServerUrl = philipsTVServerUrl;
        this.defaultRequestTimeoutInMs = Duration.ofMillis(Long.parseLong(defaultRequestTimeout));
        this.philipsHelper = philipsHelper;
        this.messageSource = messageSource;
        this.httpClient = HttpClient.newHttpClient();
    }

    private <T> T executeRequest(String endpoint, Class<T> returnType) throws URISyntaxException, IOException, InterruptedException {
        final HttpRequest httpRequest = HttpRequest.newBuilder().GET().timeout(defaultRequestTimeoutInMs).uri(new URI(philipsTVServerUrl + endpoint)).build();
        final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), returnType);
    }

    private <T> String executePostRequest(String endpoint, T requestBody) throws URISyntaxException, IOException, InterruptedException {
        final HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .timeout(defaultRequestTimeoutInMs).uri(new URI(philipsTVServerUrl + endpoint))
                .build();

        final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public PowerStateResponse getTVPowerState() {
        return (PowerStateResponse) philipsHelper.executePhilipsTVCommand("powerstate", PowerStateResponse.class);
    }

    public String getTVCurrentChannel() {
        String s = philipsHelper.executePhilipsTVCommand("current_channel");
        return s;
    }

    public String setHDMIChannel(Integer channel) {
        if (channel < 1 || channel > 3) {
            throw new IllegalArgumentException("Channel must be equal to 1, 2 or 3");
        } else {
            String result = null;

            philipsHelper.executePhilipsTVCommand("source");
            for (int i = 0; i <= NUMBER_OF_SOURCE_MENU_ITEMS; i++) {
                philipsHelper.executePhilipsTVCommand("cursor_left");
            }
            for (int j = 0; j < NUMBER_OF_SOURCE_MENU_ITEMS_BEFORE_HDMI_ITEMS + channel; j++) {
                result = philipsHelper.executePhilipsTVCommand("cursor_right");
            }

            return result;
        }
    }

    public PhilipsTVVolumeAndMuteStateResponse getTVVolume() {
        try {
            return executeRequest(TV_VOLUME_AND_MUTESTATE_ENDPOINT, PhilipsTVVolumeAndMuteStateResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setTVVolume(SetTVVolumeRequest setTVVolumeRequest) {
        try {
            PhilipsTVVolumeAndMuteStateResponse volumeInformation = executeRequest(TV_VOLUME_AND_MUTESTATE_ENDPOINT, PhilipsTVVolumeAndMuteStateResponse.class);

            if (setTVVolumeRequest.volume() > volumeInformation.getMax()) {
                throw new Http400BadRequestException(messageSource.getMessage("volume_value_not_within_valid_range", null,
                        Locale.getDefault()).formatted(volumeInformation.getMin(), volumeInformation.getMax()));
            } else {
                // To get volume: x you need to send volume: x+1 to Philips TV (bug!). Sometimes it's the other way around (unreliable...)
                String a = executePostRequest(TV_VOLUME_AND_MUTESTATE_ENDPOINT, new PhilipsTVVolumeRequest(setTVVolumeRequest.volume()));
            }
        } catch (final URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public PhilipsTVVolumeAndMuteStateResponse getTVMuteState() {
        try {
            return executeRequest(TV_VOLUME_AND_MUTESTATE_ENDPOINT, PhilipsTVVolumeAndMuteStateResponse.class);

        } catch (final URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setTVMuteState(SetTVMuteStateRequest setTVMuteStateRequest) {
        try {
            executePostRequest(TV_VOLUME_AND_MUTESTATE_ENDPOINT, new PhilipsTVMuteStateRequest(setTVMuteStateRequest.muted()));

        } catch (final URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public String setTVStandbyState(String standby) {
        return philipsHelper.executePhilipsTVCommand("standby");
    }

    public String powerOn() {
        return philipsHelper.executePhilipsTVCommand("power_on");
    }
}
