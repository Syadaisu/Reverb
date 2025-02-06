package com.reverb.app.dto.responses;

import java.util.Date;

public class AddMessageResponse {

    private String messageId;
    private int channelId;
    private int authorId;
    private String body;
    private Date creationDate;
    private String attachmentUuid;
    private String responseToId;

    public AddMessageResponse() {}

    public AddMessageResponse(String messageId, int channelId, int authorId,
                              String body, Date creationDate,
                              String attachmentUuid, String responseToId) {
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

}
