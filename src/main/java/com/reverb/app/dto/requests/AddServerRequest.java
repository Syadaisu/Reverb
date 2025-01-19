package com.reverb.app.dto.requests;

public class AddServerRequest {
    private String serverName;
    private String serverDescription;
    private int ownerId;

    public String getServerName() {
        return serverName;
    }

    public String getServerDescription() {
        return serverDescription;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
