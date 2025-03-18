package org.eriksandsten.devtools.response.browser;

import lombok.Data;

@Data
public class SetWindowBoundsResponse {
    public Long id;
    public Result result;

    @Data
    public static class Result {

    }
}
