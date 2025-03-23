package org.eriksandsten.request;

import jakarta.validation.constraints.Pattern;

public record SetCurrentTVChannelRequest(@Pattern(regexp = "^(EPG|[0-9]+)$") String channelId) {
}
