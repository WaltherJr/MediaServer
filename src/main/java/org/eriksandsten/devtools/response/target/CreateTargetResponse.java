package org.eriksandsten.devtools.response.target;

import lombok.Data;

@Data
public class CreateTargetResponse {
    private Long id;
    private Result result;

    @Data
    public static class Result {
        private String targetId;
    }
}
