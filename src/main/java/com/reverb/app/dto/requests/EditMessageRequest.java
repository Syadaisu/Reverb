package com.reverb.app.dto.requests;

public class EditMessageRequest {
    private String body;
    private Integer attachment;

    public EditMessageRequest() {
    }

    public EditMessageRequest(String body, Integer attachment) {
        this.body = body;
        this.attachment = attachment;
    }

    // Getters and Setters
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getAttachment() {
        return attachment;
    }

    public void setAttachment(Integer attachment) {
        this.attachment = attachment;
    }
}
