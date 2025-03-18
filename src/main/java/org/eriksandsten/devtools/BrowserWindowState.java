package org.eriksandsten.devtools;

import lombok.Getter;

@Getter
public enum BrowserWindowState {
    NORMAL("normal"), MINIMIZED("minimized"), MAXIMIZED("maximized"), FULLSCREEN("fullscreen");

    private final String value;

    BrowserWindowState(String value) {
        this.value = value;
    }
}
