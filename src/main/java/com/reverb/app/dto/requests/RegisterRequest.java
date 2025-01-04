package com.reverb.app.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(max = 16)
    private String name;

    @NotBlank
    @Size(max = 32)
    @Email
    private String email;

    @NotBlank
    @Size(max = 64)
    private String password;

    @NotBlank
    @Size(max = 64)
    private String confirmPassword;
}
