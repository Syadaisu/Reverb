package com.reverb.app.dto.responses;

import java.util.Date;

public class MessageDocumentDto {

    private String messageId; // Changed to String for MongoDB ObjectId
    private Integer channelId;
    private Integer authorId;
    private String body;
    private Date creationDate;
    private Boolean isDeleted;
    private String attachmentUuid;
    private String responseToId;
    private String responseTo;

    // Constructors
    public MessageDocumentDto() {}

    public MessageDocumentDto(String messageId, Integer channelId, Integer authorId, String body, Date creationDate, Boolean isDeleted, String attachmentUuid, String responseToId, String responseTo) {
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

    // Getters and Setters

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getAttachmentUuid() {
        return attachmentUuid;
    }

    public void setAttachment(String attachmentUuid) {
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