package org.eriksandsten.philips.response;

import lombok.Data;

@Data
public class VolumeResponse extends PhilipsResponse {
    private Boolean muted;
    private Short current;
    private Short min;
    private Short max;
}
