package com.reverb.app.dto.responses;

public class EditChannelResponse {
    private int channelId;
    private String name;
    private String description;
    private int serverId;

    public EditChannelResponse(int channelId, String name, String description, int serverId) {
        this.channelId = channelId;
        this.name = name;
        this.description = description;
        this.serverId = serverId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}