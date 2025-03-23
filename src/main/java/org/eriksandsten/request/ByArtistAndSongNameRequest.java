package org.eriksandsten.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ByArtistAndSongNameRequest extends SetCurrentMusicVideoRequest {
    @NotEmpty
    private String artistName;
    @NotEmpty
    private String songName;
}
