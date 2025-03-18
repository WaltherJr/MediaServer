package org.eriksandsten.devtools.request.target;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class GetTargetInfoRequest extends BaseRequest {
    private final String targetId;
    private final Boolean flatten;

    public GetTargetInfoRequest(String targetId, Boolean flatten) {
        this.targetId = targetId;
        this.flatten = flatten;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
                "id", 1,
                "method", "Target.getTargetInfo",
                "params", Map.of(
                        "targetId", targetId,
                        "flatten", flatten
                )
        ));
    }
}
