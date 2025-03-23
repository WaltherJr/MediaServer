package org.eriksandsten.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlayState {
    @JsonProperty(value = "paused") PAUSED, @JsonProperty("playing") PLAYING
}
