package org.eriksandsten.devtools.request.target;

import lombok.Getter;

@Getter
public enum TargetType {
    PAGE("page");

    private final String type;

    TargetType(String type) {
        this.type = type;
    }
}
