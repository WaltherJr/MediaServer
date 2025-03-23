package org.eriksandsten.devtools.response.browser;

import lombok.Data;

@Data
public class SetWindowBoundsResponse {
    private Long id;
    private Result result;

    @Data
    public static class Result {

    }
}
