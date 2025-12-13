package org.eriksandsten.philips.response;

public class PhilipsTVVolumeAndMuteStateResponse extends PhilipsResponse {
    private Boolean muted;
    private Short current;
    private Short min;
    private Short max;

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }

    public Short getCurrent() {
        return current;
    }

    public void setCurrent(Short current) {
        this.current = current;
    }

    public Short getMin() {
        return min;
    }

    public void setMin(Short min) {
        this.min = min;
    }

    public Short getMax() {
        return max;
    }

    public void setMax(Short max) {
        this.max = max;
    }
}
