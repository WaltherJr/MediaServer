package org.eriksandsten.devtools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum BrowserWindowState {
    @JsonProperty(value = "normal") NORMAL("normal"),
    @JsonProperty(value = "minimized") MINIMIZED("minimized"),
    @JsonProperty(value = "maximized") MAXIMIZED("maximized"),
    @JsonProperty(value = "fullscreen") FULLSCREEN("fullscreen");

    private final String value;

    BrowserWindowState(String value) {
        this.value = value;
    }
}
