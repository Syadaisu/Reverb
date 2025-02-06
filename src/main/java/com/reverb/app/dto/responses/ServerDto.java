package com.reverb.app.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerDto {
    private int serverId;
    private String serverName;
    private String description;
    private int ownerId;
    private String serverIconUuid;

    public ServerDto(int serverId, String serverName, String description, int ownerId, String serverIconUuid) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.description = description;
        this.ownerId = ownerId;
        this.serverIconUuid = serverIconUuid;
    }

    // Getters and setters
    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getServerIconUuid() {
        return serverIconUuid;
    }

    public void setServerIconUuid(String serverIconUuid) {
        this.serverIconUuid = serverIconUuid;
    }

    @Override
    public String toString() {
        return "ServerDto{" +
                "serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", description='" + description + '\'' +
                ", ownerId=" + ownerId +
                ", serverIconUuid=" + serverIconUuid +
                '}';
    }
}
