package org.eriksandsten.domain.allente.channel;

import java.util.ArrayList;
import java.util.List;

public class TVChannel extends AllenteChannel {
    public static final List<TVChannel> subscribedTVChannels = new ArrayList<>();
    private String channelId;
    private String mappedChannelId;
    private String channelName;

    static {
        subscribedTVChannels.add(new TVChannel("0148", "20001", "SVT1")); // SVT1
        subscribedTVChannels.add(new TVChannel("0282", "20002", "SVT2")); // SVT2
        subscribedTVChannels.add(new TVChannel("0290", "20003", "TV3")); // TV3
        subscribedTVChannels.add(new TVChannel("0227", "20004", "TV4")); // TV4
        subscribedTVChannels.add(new TVChannel("0279", "20005", "Kanal 5")); // Kanal 5
        subscribedTVChannels.add(new TVChannel("0360", "20064", "Kanal 6")); // Kanal 6
        subscribedTVChannels.add(new TVChannel("0232", "20007", "Kanal 7")); // Kanal 7
        subscribedTVChannels.add(new TVChannel("666", "20008", "Kanal 8"));  // Kanal 8
        subscribedTVChannels.add(new TVChannel("474", "20009", "Kanal 9"));  // Kanal 9
        subscribedTVChannels.add(new TVChannel("667", "20060", "Kanal 10"));  // Kanal 10
        subscribedTVChannels.add(new TVChannel("0235", "20011", "Kanal 11")); // Kanal 11
        subscribedTVChannels.add(new TVChannel("664", "20012", "Kanal 12"));  // Kanal 12
        subscribedTVChannels.add(new TVChannel("1000", "20104", "ATG Live")); // ATG Live
        subscribedTVChannels.add(new TVChannel("722", "20093", "Godare"));  // Godare
        subscribedTVChannels.add(new TVChannel("0149", "20013", "Kunskapskanalen HD")); // Kunskapskanalen HD
        subscribedTVChannels.add(new TVChannel("1053", "20122", "AXESS TV")); // AXESS TV
        subscribedTVChannels.add(new TVChannel("0147", "20010", "SVT Barn HD")); // SVT Barn HD
        subscribedTVChannels.add(new TVChannel("EPG", "EPG", "EPG"));
    }

    public TVChannel(String channelId) {
        subscribedTVChannels.stream().filter(channel -> channel.getChannelId().equals(channelId)).findFirst().ifPresent((foundChannel) -> {
            this.channelId = foundChannel.getChannelId();
            this.mappedChannelId = foundChannel.getMappedChannelId();
            this.channelName = foundChannel.getChannelName();
        });
    }

    public TVChannel(String channelId, String mappedChannelId, String channelName) {
        super(ChannelType.TV_CHANNEL);
        this.channelId = channelId;
        this.mappedChannelId = mappedChannelId;
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getMappedChannelId() {
        return mappedChannelId;
    }

    public void setMappedChannelId(String mappedChannelId) {
        this.mappedChannelId = mappedChannelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public static TVChannel getEPG() {
        return EPG;
    }

    public static void setEPG(TVChannel EPG) {
        TVChannel.EPG = EPG;
    }

    public static TVChannel getMappedChannelById(String channelId) {
        return subscribedTVChannels.stream()
                .filter(channel -> channel.getMappedChannelId().equals(channel.getChannelId()))
                .findFirst()
                .orElse(null);
    }

    public static TVChannel EPG = new TVChannel("EPG", "EPG", "EPG");
}
