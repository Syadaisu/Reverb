package com.reverb.app.dto.requests;

public class AddChannelRequest {
    private String channelName;
    private int serverId;
    private String description;

    public AddChannelRequest() {
    }

    public AddChannelRequest(String channelName, int serverId, String description) {
        this.channelName = channelName;
        this.serverId = serverId;
        this.description = description;
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
