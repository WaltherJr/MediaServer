package org.eriksandsten.devtools.request.target;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class CreateTargetRequest extends BaseRequest {
    private final String url;

    public CreateTargetRequest(String url) {
        this.url = url;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
                "id", 1,
                "method", "Target.createTarget",
                "params", Map.of(
                        "url", url
                )
        ));
    }
}
