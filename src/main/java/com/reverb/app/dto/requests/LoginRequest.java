package com.reverb.app.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Size(max = 32)
    @Email
    private String email;

    @NotBlank
    @Size(max = 64)
    private String password;
}
