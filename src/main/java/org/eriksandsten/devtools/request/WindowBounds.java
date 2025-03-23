package org.eriksandsten.devtools.request;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.eriksandsten.devtools.BrowserWindowState;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class WindowBounds extends BaseRequest {
    private static final Set<BrowserWindowState> DIMENSIONLESS_WINDOWSTATES = Set.of(BrowserWindowState.MINIMIZED, BrowserWindowState.MAXIMIZED, BrowserWindowState.FULLSCREEN);

    // TODO: should not depend on how
    public Integer left;
    public Integer top;
    public Integer width;
    public Integer height;
    public String windowState;
    public Boolean dimensionless;

    public WindowBounds(BrowserWindowState windowState) {
        this.windowState = windowState.getValue();
        this.dimensionless = true;
    }

    public WindowBounds(Integer left, Integer top, Integer width, Integer height, BrowserWindowState windowState) {
        dimensionless = DIMENSIONLESS_WINDOWSTATES.contains(windowState);

        if (dimensionless && !Stream.of(left, top, width, height).filter(Objects::nonNull).toList().isEmpty()) {
            throw new IllegalArgumentException("The 'minimized', 'maximized' and 'fullscreen' states cannot be combined with 'left', 'top', 'width' or 'height'");
        }
    }

    @Override
    public String getJSON() throws JsonProcessingException {
        return objectMapper.writeValueAsString(Map.of(
            "left", left,
            "top", top,
            "width", width,
            "height", height,
            "windowState", windowState
        ));
    }
}
