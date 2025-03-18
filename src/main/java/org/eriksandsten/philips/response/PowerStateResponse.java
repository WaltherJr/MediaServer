package org.eriksandsten.philips.response;

import lombok.Data;

@Data
public class PowerStateResponse extends PhilipsResponse {
    private String powerstate;
}
