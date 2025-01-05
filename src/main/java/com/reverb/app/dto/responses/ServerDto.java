package com.reverb.app.dto.responses;

import lombok.Data;
import java.util.Date;

@Data
public class ServerDto {
    private int serverId;
    private String serverName;
    private String description;
    private byte[] avatar;
    private int ownerId;

    public ServerDto(int serverId, String serverName, String description, byte[] avatar, int ownerId) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.description = description;
        this.avatar = avatar;
        this.ownerId = ownerId;
    }

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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}