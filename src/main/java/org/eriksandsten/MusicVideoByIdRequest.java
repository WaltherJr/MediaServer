package org.eriksandsten;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record MusicVideoByIdRequest(@NotEmpty @Pattern(regexp = "[a-zA-Z0-9]+") String videoId) {
}
