package com.reverb.app.dto.responses;

public class DeleteChannelResponse {
    private int channelId;

    public DeleteChannelResponse(int channelId) {
        this.channelId = channelId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}