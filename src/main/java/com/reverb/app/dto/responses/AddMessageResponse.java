// src/main/java/com/example/dto/AddMessageResponse.java
package com.reverb.app.dto.responses;

import java.util.Date;

// This matches the JSON structure you want to return
public class AddMessageResponse {

    private String messageId;
    private int channelId;
    private int authorId;
    private String body;
    private Date creationDate;
    private boolean isDeleted;
    private String attachmentUuid;
    private String responseToId;
    private String responseTo;

    public AddMessageResponse() {}

    public AddMessageResponse(String messageId, int channelId, int authorId,
                              String body, Date creationDate, boolean isDeleted,
                              String attachmentUuid, String responseToId, String responseTo) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.authorId = authorId;
        this.body = body;
        this.creationDate = creationDate;
        this.isDeleted = isDeleted;
        this.attachmentUuid = attachmentUuid;
        this.responseToId = responseToId;
        this.responseTo = responseTo;
    }

    // Getters and setters
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
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

    public String getAttachmentUuid() {
        return attachmentUuid;
    }
    public void setAttachmentUuid(String attachmentUuid) {
        this.attachmentUuid = attachmentUuid;
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
