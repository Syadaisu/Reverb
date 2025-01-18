// src/main/java/com/example/dto/AddMessageRequest.java
package com.reverb.app.dto.requests;

public class AddMessageRequest {

    private int channelId;      // which channel
    private int authorId;       // which user
    private String body;        // message text
    private String responseToId;
    private String responseTo;
    private int attachment;     // optional, default 0

    public AddMessageRequest() {}

    public AddMessageRequest(int channelId, int authorId, String body,
                             String responseToId, String responseTo, int attachment) {
        this.channelId = channelId;
        this.authorId = authorId;
        this.body = body;
        this.responseToId = responseToId;
        this.responseTo = responseTo;
        this.attachment = attachment;
    }

    // Getters and setters
    public int getChannelId() {
        return channelId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public int getAttachment() {
        return attachment;
    }
    public void setAttachment(int attachment) {
        this.attachment = attachment;
    }
}
