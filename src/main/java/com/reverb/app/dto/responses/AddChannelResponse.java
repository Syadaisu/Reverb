package com.reverb.app.dto.responses;

public class AddChannelResponse {
    private int channelId;
    private String channelName;
    private String description;
    private String roleAccess;


    public AddChannelResponse() {
    }

    public AddChannelResponse(int channelId, String channelName, String description, String roleAccess) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.description = description;
        this.roleAccess = roleAccess;
    }

    // Getters and setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(String roleAccess) {
        this.roleAccess = roleAccess;
    }
}
