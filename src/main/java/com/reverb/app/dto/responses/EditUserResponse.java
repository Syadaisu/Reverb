package com.reverb.app.dto.responses;

public class EditUserResponse {
    private String message;

    public EditUserResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
