package com.reverb.app.dto.requests;

public class EditChannelRequest {
    private String channelName;
    private String description;

    public EditChannelRequest() {
    }

    public EditChannelRequest(String channelName,  String description) {
        this.channelName = channelName;
        this.description = description;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
