package org.eriksandsten.devtools.request.dom;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class GetOuterHTMLRequest extends BaseRequest {
    private final String sessionId;
    private final Long nodeId;

    public GetOuterHTMLRequest(String sessionId, Long nodeId) {
        this.sessionId = sessionId;
        this.nodeId = nodeId;
    }


    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "id", 1,
            "sessionId", sessionId,
            "method", "DOM.getOuterHTML",
            "params", Map.of(
                    "nodeId", nodeId
            )
        ));
    }
}
