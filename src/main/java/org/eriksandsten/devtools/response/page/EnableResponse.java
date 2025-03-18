package org.eriksandsten.devtools.response.page;

import lombok.Data;

@Data
public class EnableResponse {
    public Long id;
    public Result result;
    public String sessionId;

    @Data
    public static class Result {
    }
}
