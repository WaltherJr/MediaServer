package org.eriksandsten.domain.allente.channel;

public abstract class AllenteChannel {
    private ChannelType channelType;

    public AllenteChannel() {
    }

    public AllenteChannel(ChannelType channelType) {
        this.channelType = channelType;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }
}
