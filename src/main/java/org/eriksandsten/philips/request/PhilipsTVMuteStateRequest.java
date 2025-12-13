package org.eriksandsten.philips.request;

public class PhilipsTVMuteStateRequest {
    private boolean muted;

    public PhilipsTVMuteStateRequest(boolean muted) {
        this.muted = muted;
    }

    @Override
    public String toString() {
        return "{\"muted\": %s}".formatted(String.valueOf(muted));
    }
}
