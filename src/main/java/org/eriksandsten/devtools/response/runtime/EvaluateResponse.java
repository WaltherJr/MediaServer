package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

@Data
public class EvaluateResponse {
    private Long id;
    private Result result;
    private String sessionId;

    @Data
    public static class Result {
        private String type;
        private String subtype;
        private String className;
        private EvalResult result;
    }

    @Data
    public static class EvalResult {
        private String type;
        private String value;
        private String description;
    }
}
