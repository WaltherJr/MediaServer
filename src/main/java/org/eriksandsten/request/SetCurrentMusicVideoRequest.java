package org.eriksandsten.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
    @JsonSubTypes.Type(ByVideoIdRequest.class),
    @JsonSubTypes.Type(ByArtistAndSongNameRequest.class)
})
public abstract class SetCurrentMusicVideoRequest {
}
