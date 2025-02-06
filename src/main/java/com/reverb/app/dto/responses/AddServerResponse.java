package com.reverb.app.dto.responses;

public class AddServerResponse {
    private int serverId;
    private String serverName;
    private String description;
    private String errorMessage; // Optional field for error messages

    public AddServerResponse() {
    }

    public AddServerResponse(int serverId, String serverName, String description) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.description = description;
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


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
