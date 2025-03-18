package org.eriksandsten.philips;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eriksandsten.philips.response.PhilipsResponse;
import org.eriksandsten.philips.response.PowerStateResponse;
import org.eriksandsten.philips.response.VolumeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Service
public class PhilipsTVService {
    private final String pylipsExecutable;
    private static final String pylipsResponseBody = "Sending (GET|POST) request to (.+)\\s+Request sent!";
    private static final int NUMBER_OF_SOURCE_MENU_ITEMS = 7;
    private static final int NUMBER_OF_SOURCE_MENU_ITEMS_BEFORE_HDMI_ITEMS = 3;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(PhilipsTVService.class);

    public PhilipsTVService(@Value("${pylips_location}") String pylipsExecutable) {
        this.pylipsExecutable = pylipsExecutable;
    }

    public String setTVMuteState(String muted) {
        executePhilipsTVCommand("source");
        return executePhilipsTVCommand("cursor_left");
    }

    public PowerStateResponse getTVPowerState() {
        return (PowerStateResponse) executePhilipsTVCommand("powerstate", PowerStateResponse.class);
    }

    public String getTVCurrentChannel() {
        String s = executePhilipsTVCommand("current_channel");
        return s;
    }

    public String setHDMIChannel(Integer channel) {
        if (channel < 1 || channel > 3) {
            throw new IllegalArgumentException("Channel must be equal to 1, 2 or 3");
        } else {
            String result = null;

            executePhilipsTVCommand("source");
            for (int i = 0; i <= NUMBER_OF_SOURCE_MENU_ITEMS; i++) {
                executePhilipsTVCommand("cursor_left");
            }
            for (int j = 0; j < NUMBER_OF_SOURCE_MENU_ITEMS_BEFORE_HDMI_ITEMS + channel; j++) {
                result = executePhilipsTVCommand("cursor_right");
            }

            return result;
        }
    }

    public VolumeResponse getTVVolume() {
        return (VolumeResponse) executePhilipsTVCommand("volume", VolumeResponse.class);
    }

    public String setTVStandbyState(String standby) {
        return executePhilipsTVCommand("standby");
    }

    public String powerOn() {
        return executePhilipsTVCommand("power_on");
    }

    public PhilipsResponse executePhilipsTVCommand(String command, Class<? extends PhilipsResponse> responseType) {
        String responseBody = executePhilipsTVCommand(command);
        return parseResponse(responseBody, responseType);
    }

    public String executePhilipsTVCommand(String command) {
        try {
            Scanner sc = new java.util.Scanner(Runtime.getRuntime()
                    .exec("python -m pylips --command " + command, null, new File(pylipsExecutable))
                    .getInputStream()).useDelimiter("\\A");
            return sc.hasNext() ? sc.next() : "";
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private PhilipsResponse parseResponse(String responseBody, Class<? extends PhilipsResponse> responseType) {
        final String[] splitted = responseBody.split(pylipsResponseBody);

        if (splitted.length > 1) {
            try {
                return objectMapper.readValue(splitted[1].trim(), responseType);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                return null;
            }
        } else {
            return null;
        }
    }
}
