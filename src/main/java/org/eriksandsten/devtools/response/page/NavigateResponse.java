package org.eriksandsten.devtools.response.page;

import lombok.Data;

@Data
public class NavigateResponse {
    private Long id;
    private Result result;
    private String sessionId;

    @Data
    public static class Result {
        private String frameId;
        private String loaderId;
        private String errorText;
    }
}
