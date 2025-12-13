package org.eriksandsten.philips.response;

public class PowerStateResponse extends PhilipsResponse {
    private String powerstate;

    public String getPowerstate() {
        return powerstate;
    }

    public void setPowerstate(String powerstate) {
        this.powerstate = powerstate;
    }
}
