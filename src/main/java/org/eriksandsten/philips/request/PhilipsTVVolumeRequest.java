package org.eriksandsten.philips.request;

public class PhilipsTVVolumeRequest {
    private short volume;

    public PhilipsTVVolumeRequest(short volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "{\"current\": \"%d\"}".formatted(volume);
    }
}
