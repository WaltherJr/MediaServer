package org.eriksandsten.devtools.response.browser;

import lombok.Data;
import org.eriksandsten.devtools.response.WindowBounds;

@Data
public class GetWindowForTargetResponse {
    public Long id;
    public Result result;

    @Data
    public static class Result {
        public Integer windowId;
        public WindowBounds bounds;
    }
}
