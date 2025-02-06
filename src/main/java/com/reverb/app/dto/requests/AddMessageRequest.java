package com.reverb.app.dto.requests;

public class AddMessageRequest {

    private int channelId;      // which channel
    private int authorId;       // which user
    private String body;        // message text
    private String responseToId;
    private String attachmentUuid;     // optional, default 0

    public AddMessageRequest() {}

    public AddMessageRequest(int channelId, int authorId, String body,
                             String responseToId , String attachmentUuid) {
        this.channelId = channelId;
        this.authorId = authorId;
        this.body = body;
        this.responseToId = responseToId;
        this.attachmentUuid = attachmentUuid;
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

    public String getResponseToId() {
        return responseToId;
    }
    public void setResponseToId(String responseToId) {
        this.responseToId = responseToId;
    }


    public String getAttachmentUuid() {
        return attachmentUuid;
    }
    public void setAttachmentUuid(String attachmentUuid) {
        this.attachmentUuid = attachmentUuid;
    }

}
