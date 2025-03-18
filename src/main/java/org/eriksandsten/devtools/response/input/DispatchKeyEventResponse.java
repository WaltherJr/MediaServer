package org.eriksandsten.devtools.response.input;

import lombok.Data;

public class DispatchKeyEventResponse {
    public Long id;
    public Result result;

    @Data
    public static class Result {
    }
}
