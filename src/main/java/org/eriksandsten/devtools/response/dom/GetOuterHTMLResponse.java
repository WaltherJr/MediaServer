package org.eriksandsten.devtools.response.dom;

import lombok.Data;

@Data
public class GetOuterHTMLResponse {
    private Long id;
    private Result result;
    private String sessionId;

    @Data
    public static class Result {
        private String outerHTML;
    }
}
