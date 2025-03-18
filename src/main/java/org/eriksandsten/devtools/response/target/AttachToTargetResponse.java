package org.eriksandsten.devtools.response.target;

import lombok.Data;

@Data
public class AttachToTargetResponse {
    private Long id;
    private Result result;

    @Data
    public static class Result {
        private String sessionId;
    }
}
