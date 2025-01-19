package com.reverb.app.dto.requests;

public class EditChannelRequest {
    private String channelName;
    //private String roleAccess;
    private String description;

    // Constructors
    public EditChannelRequest() {
    }

    public EditChannelRequest(String channelName,  String description) {
        this.channelName = channelName;
        //this.roleAccess = roleAccess;
        this.description = description;
    }

    // Getters and Setters
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /*public String getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(String roleAccess) {
        this.roleAccess = roleAccess;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
