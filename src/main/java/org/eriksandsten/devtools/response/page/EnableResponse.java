package org.eriksandsten.devtools.response.page;

import lombok.Data;

@Data
public class EnableResponse {
    private Long id;
    private Result result;
    private String sessionId;

    @Data
    public static class Result {
    }
}
