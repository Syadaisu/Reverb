package com.reverb.app.dto.responses;

public class ChannelDto {
    private int channelId;
    private String channelName;
    private int serverId;    // so the client knows which server this channel belongs to
    private String description;

    public ChannelDto() {
    }

    public ChannelDto(int channelId, String channelName, int serverId, String description) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.serverId = serverId;
        this.description = description;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
