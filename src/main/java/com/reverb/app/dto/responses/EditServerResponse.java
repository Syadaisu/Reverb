package com.reverb.app.dto.responses;

public class EditServerResponse {
    private String message;

    public EditServerResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
