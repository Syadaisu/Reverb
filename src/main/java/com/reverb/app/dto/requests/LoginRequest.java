package com.reverb.app.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public @NotBlank  String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }
}
