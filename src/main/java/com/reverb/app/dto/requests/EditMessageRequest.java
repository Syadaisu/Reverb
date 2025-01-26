package com.reverb.app.dto.requests;

public class EditMessageRequest {
    private String body;
    private String attachmentUuid;

    public EditMessageRequest() {
    }

    public EditMessageRequest(String body, String attachmentUuid) {
        this.body = body;
        this.attachmentUuid = attachmentUuid;
    }

    // Getters and Setters
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachmentUuid() {
        return attachmentUuid;
    }

    public void setAttachmentUuid(String attachment) {
        this.attachmentUuid = attachmentUuid;
    }
}
