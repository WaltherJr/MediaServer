package org.eriksandsten.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ByVideoIdRequest extends SetCurrentMusicVideoRequest {
    @NotEmpty @Pattern(regexp = "[a-zA-Z0-9]+")
    private String videoId;
}
