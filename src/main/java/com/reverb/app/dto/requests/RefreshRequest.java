package com.reverb.app.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotBlank
    public String token;

    public @NotBlank String getToken() {
        return token;
    }
}
