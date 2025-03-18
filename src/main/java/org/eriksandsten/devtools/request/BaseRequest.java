package org.eriksandsten.devtools.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseRequest {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public abstract String getJSON() throws JsonProcessingException;
}
