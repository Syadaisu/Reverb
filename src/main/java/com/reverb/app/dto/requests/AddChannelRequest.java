package com.reverb.app.dto.requests;

public class AddChannelRequest {
    private String channelName;
    private int serverId;
    private String roleAccess;
    private String description;

    // Constructors
    public AddChannelRequest() {
    }

    public AddChannelRequest(String channelName, int serverId, String description, String roleAccess) {
        this.channelName = channelName;
        this.serverId = serverId;
        this.roleAccess = roleAccess;
        this.description = description;
    }

    // Getters and Setters
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

    public String getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(String roleAccess) {
        this.roleAccess = roleAccess;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
