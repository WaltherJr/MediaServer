package org.eriksandsten.request;

import jakarta.validation.constraints.NotEmpty;

public class ByArtistAndSongNameRequest extends SetCurrentMusicVideoRequest {
    @NotEmpty
    private String artistName;
    @NotEmpty
    private String songName;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
