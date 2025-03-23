package org.eriksandsten.response;

import lombok.Data;
import org.eriksandsten.domain.allente.channel.TVChannel;
import java.util.List;

@Data
public class GetAllTVChannelsResponse {
    private List<TVChannel> channels;

    public GetAllTVChannelsResponse(List<TVChannel> channels) {
        this.channels = channels;
    }
}
