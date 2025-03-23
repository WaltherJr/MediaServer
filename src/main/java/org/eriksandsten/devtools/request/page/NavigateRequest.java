package org.eriksandsten.devtools.request.page;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;

import java.util.Map;

public class NavigateRequest extends BaseRequest {
    private String sessionId;
    private String url;

    public NavigateRequest(String sessionId, String url) {
        this.sessionId = sessionId;
        this.url = url;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
                "id", 1,
                "sessionId", sessionId,
                "method", "Page.navigate",
                "params", Map.of(
                "url", url
                )
        ));
    }
}
