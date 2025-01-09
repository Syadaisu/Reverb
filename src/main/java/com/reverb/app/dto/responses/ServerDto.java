package com.reverb.app.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerDto {
    private int serverId;
    private String serverName;
    private String description;
    private Boolean isPublic;

    public ServerDto(int serverId, String serverName, String description, Boolean isPublic) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.description = description;
        this.isPublic = isPublic;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String toString() {
        return "ServerDto{" +
                "serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
