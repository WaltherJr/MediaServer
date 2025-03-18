package org.eriksandsten.devtools.response.target;

import lombok.Data;
import java.util.List;

@Data
public class GetBrowserContextsResponse {
    private Long id;
    private GetBrowserContextsResponse.Result result;

    @Data
    public static class Result {
        private List<String> browserContextIds;
    }
}
