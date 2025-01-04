package com.reverb.app.dto.responses;

public class GenericResponse {
    public String status;
    public String message;

    public GenericResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
