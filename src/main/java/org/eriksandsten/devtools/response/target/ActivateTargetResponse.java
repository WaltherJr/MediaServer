package org.eriksandsten.devtools.response.target;

import lombok.Data;

@Data
public class ActivateTargetResponse {
    private Long id;
    private ActivateTargetResponse.Result result;

    @Data
    public static class Result {
        private String sessionId;
    }
}
