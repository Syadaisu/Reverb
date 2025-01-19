package com.reverb.app.dto.requests;


import jakarta.validation.constraints.NotBlank;

public class CreateServerRequest {

    @NotBlank
    private String serverName;

    private String description;

    @NotBlank
    private boolean isPublic;

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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}