package com.reverb.app.dto.responses;

public class EditServerResponse {
    private int serverId;
    private String name;
    private String description;

    public EditServerResponse(int serverId, String name, String description) {
        this.serverId = serverId;
        this.name = name;
        this.description = description;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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
}