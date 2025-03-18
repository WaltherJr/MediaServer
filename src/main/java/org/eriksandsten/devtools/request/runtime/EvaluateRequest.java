package org.eriksandsten.devtools.request.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class EvaluateRequest extends BaseRequest {
    private final String sessionId;
    private final String expression;

    public EvaluateRequest(String sessionId, String expression) {
        this.sessionId = sessionId;
        this.expression = expression;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "id", 1,
            "sessionId", sessionId,
            "method", "Runtime.evaluate",
            "params", Map.of(
                    "expression", expression
                )
        ));
    }
}
