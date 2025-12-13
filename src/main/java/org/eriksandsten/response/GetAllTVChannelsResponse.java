package org.eriksandsten.response;

import org.eriksandsten.domain.allente.channel.TVChannel;
import java.util.List;

public class GetAllTVChannelsResponse {
    private List<TVChannel> channels;

    public GetAllTVChannelsResponse(List<TVChannel> channels) {
        this.channels = channels;
    }

    public List<TVChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<TVChannel> channels) {
        this.channels = channels;
    }
}
