package org.eriksandsten.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eriksandsten.philips.response.PhilipsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class PhilipsHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String pylipsExecutable;
    private static final String pylipsResponseBody = "Sending (GET|POST) request to (.+)\\s+Request sent!";

    public PhilipsHelper(@Value("${pylips_location}") String pylipsExecutable) {
        this.pylipsExecutable = pylipsExecutable;
    }

    public PhilipsResponse executePhilipsTVCommand(String command, Class<? extends PhilipsResponse> responseType) {
        String responseBody = executePhilipsTVCommand(command);
        return parseResponse(responseBody, responseType);
    }

    public String executePhilipsTVCommand(String commandStr) {
        try {
            final String[] command = {"python", "-m", "pylips", "--command", commandStr};
            String stdout;

            final ProcessBuilder processBuilder = new ProcessBuilder(command);
            final Process process = processBuilder.start();
            process.waitFor();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                stdout = reader.lines().collect(Collectors.joining("\n"));
            }

            return stdout;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private PhilipsResponse parseResponse(String responseBody, Class<? extends PhilipsResponse> responseType) {
        final String[] splitted = responseBody.split(pylipsResponseBody);

        if (splitted.length > 1) {
            try {
                return objectMapper.readValue(splitted[1].trim(), responseType);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getCause());
            }
        } else {
            return null;
        }
    }
}
