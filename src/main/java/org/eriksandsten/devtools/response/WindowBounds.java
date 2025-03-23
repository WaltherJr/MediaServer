package org.eriksandsten.devtools.response;

import lombok.Data;
import org.eriksandsten.devtools.BrowserWindowState;

@Data
public class WindowBounds {
    private Integer left;
    private Integer top;
    private Integer width;
    private Integer height;
    private BrowserWindowState windowState;
}
