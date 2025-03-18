package org.eriksandsten.devtools.request.dom;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class GetDocumentRequest extends BaseRequest {
    private final String sessionId;
    private final Integer depth;
    private final Boolean pierce;

    public GetDocumentRequest(String sessionId, Integer depth, Boolean pierce) {
        this.sessionId = sessionId;
        this.depth = depth;
        this.pierce = pierce;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "id", 1,
            "sessionId", sessionId,
            "method", "DOM.getDocument",
            "params", Map.of(
                "depth", depth,
                "pierce", pierce
            )
        ));
    }
}
