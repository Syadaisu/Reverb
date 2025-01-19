package com.reverb.app.dto.responses;

public class DeleteMessageResponse {
    private int messageId;

    public DeleteMessageResponse(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}