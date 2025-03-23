package org.eriksandsten.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record SetTVVolumeRequest(@NotEmpty @Min(0) Short volume) {}
