package org.eriksandsten.devtools.request.browser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class GetWindowForTargetRequest extends BaseRequest {
    private final String targetId;
    private final Boolean flatten;

    public GetWindowForTargetRequest(String targetId, Boolean flatten) {
        this.targetId = targetId;
        this.flatten = flatten;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
                "id", 1,
                "method", "Browser.getWindowForTarget",
                "params", Map.of(
                        "targetId", targetId,
                        "flatten", flatten
                )
        ));
    }
}
