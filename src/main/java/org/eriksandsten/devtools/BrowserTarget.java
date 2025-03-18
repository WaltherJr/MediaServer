package org.eriksandsten.devtools;

import lombok.Data;

@Data
public class BrowserTarget {
    private String description;
    private String devtoolsFrontendUrl;
    private String id;
    private String title;
    private String type;
    private String url;
    private String webSocketDebuggerUrl;
    private String faviconUrl;
    private String parentId;
}
