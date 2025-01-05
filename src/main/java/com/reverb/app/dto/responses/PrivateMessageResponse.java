package com.reverb.app.dto.responses;

import java.util.Date;
import java.util.UUID;

public class PrivateMessageResponse {
    private int messageId;
    private String receiverId;
    private String authorId;
    private String body;
    private Date creationDate;
    private Integer responseToId;
    private UUID attachment;

    // Getters and setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
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

    public Integer getResponseToId() {
        return responseToId;
    }

    public void setResponseToId(Integer responseToId) {
        this.responseToId = responseToId;
    }

    public UUID getAttachment() {
        return attachment;
    }

    public void setAttachment(UUID attachment) {
        this.attachment = attachment;
    }
}