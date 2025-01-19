package com.reverb.app.dto.requests;


public class EditServerRequest {
    private String serverName;
    private String description;
    //private String avatar;

    public EditServerRequest() {
    }

    public EditServerRequest(String serverName, String description ) {
        this.serverName = serverName;
        this.description = description;
        //this.avatar = avatar;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }*/
}

