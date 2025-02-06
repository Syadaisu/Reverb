package com.reverb.app.dto.responses;

import java.util.Date;

public class MessageDocumentDto {
    private String messageId; // Changed to String for MongoDB ObjectId
    private Integer channelId;
    private Integer authorId;
    private String body;
    private Date creationDate;
    private String attachmentUuid;
    private String responseToId;

    public MessageDocumentDto() {}

    public MessageDocumentDto(String messageId, Integer channelId, Integer authorId, String body, Date creationDate,  String attachmentUuid, String responseToId) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.authorId = authorId;
        this.body = body;
        this.creationDate = creationDate;
        this.attachmentUuid = attachmentUuid;
        this.responseToId = responseToId;
    }

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

}