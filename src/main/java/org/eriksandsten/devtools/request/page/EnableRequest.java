package org.eriksandsten.devtools.request.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class EnableRequest extends BaseRequest {
    private String sessionId;

    public EnableRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "id", 1,
            "sessionId", sessionId,
            "method", "Page.enable"
        ));
    }
}
