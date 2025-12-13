package org.eriksandsten.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class ByVideoIdRequest extends SetCurrentMusicVideoRequest {
    @NotEmpty @Pattern(regexp = "[a-zA-Z0-9]+")
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
