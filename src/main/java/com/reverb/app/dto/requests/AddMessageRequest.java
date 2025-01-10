package com.reverb.app.dto.requests;

public class AddMessageRequest {

    private Integer channelId;
    private String body;
    private String responseToId;
    private String responseTo;
    private Integer attachment;

    public AddMessageRequest() {
    }

    public AddMessageRequest(Integer channelId, String body, String responseToId,
                             String responseTo, Integer attachment) {
        this.channelId = channelId;
        this.body = body;
        this.responseToId = responseToId;
        this.responseTo = responseTo;
        this.attachment = attachment;
    }

    // Getters and Setters
    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResponseToId() {
        return responseToId;
    }

    public void setResponseToId(String responseToId) {
        this.responseToId = responseToId;
    }

    public String getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(String responseTo) {
        this.responseTo = responseTo;
    }

    public Integer getAttachment() {
        return attachment;
    }

    public void setAttachment(Integer attachment) {
        this.attachment = attachment;
    }
}
