package org.eriksandsten.devtools.request.browser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.WindowBounds;
import org.eriksandsten.devtools.request.BaseRequest;
import java.util.Map;

public class SetWindowBoundsRequest extends BaseRequest {
    private final Integer windowId;
    private final WindowBounds bounds;
    private final Boolean flatten;

    public SetWindowBoundsRequest(Integer windowId, WindowBounds bounds, Boolean flatten) {
        this.windowId = windowId;
        this.bounds = bounds;
        this.flatten = flatten;
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "id", 1,
            "method", "Browser.setWindowBounds",
            "params", Map.of(
                    "windowId", windowId,
                    "bounds", bounds,
                    "flatten", flatten
            )
        ));
    }
}
