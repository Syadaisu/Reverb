// src/main/java/com/example/dto/AddMessageResponse.java
package com.reverb.app.dto.responses;

import java.util.Date;

// This matches the JSON structure you want to return
public class AddMessageResponse {

    private int messageId;
    private int channelId;
    private int authorId;
    private String body;
    private Date creationDate;
    private boolean isDeleted;
    private int attachment;
    private String responseToId;
    private String responseTo;

    public AddMessageResponse() {}

    public AddMessageResponse(int messageId, int channelId, int authorId,
                              String body, Date creationDate, boolean isDeleted,
                              int attachment, String responseToId, String responseTo) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.authorId = authorId;
        this.body = body;
        this.creationDate = creationDate;
        this.isDeleted = isDeleted;
        this.attachment = attachment;
        this.responseToId = responseToId;
        this.responseTo = responseTo;
    }

    // Getters and setters
    public int getMessageId() {
        return messageId;
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

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

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getAttachment() {
        return attachment;
    }
    public void setAttachment(int attachment) {
        this.attachment = attachment;
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
}
