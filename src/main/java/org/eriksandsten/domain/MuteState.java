package org.eriksandsten.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MuteState {
    @JsonProperty(value = "muted") MUTED, @JsonProperty("unmuted") UNMUTED;

    public static MuteState fromBoolean(boolean muted) {
        return muted ? MuteState.MUTED : MuteState.UNMUTED;
    }
}
