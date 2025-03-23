package org.eriksandsten.domain.allente.channel;

import lombok.Data;

@Data
public abstract class AllenteChannel {
    private ChannelType channelType;

    public AllenteChannel() {
    }

    public AllenteChannel(ChannelType channelType) {
        this.channelType = channelType;
    }
}
