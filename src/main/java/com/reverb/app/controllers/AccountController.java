package com.reverb.app.controllers;

import com.reverb.app.dto.requests.LoginRequest;
import com.reverb.app.dto.requests.RefreshRequest;
import com.reverb.app.dto.requests.RegisterRequest;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.dto.responses.LoginResponse;
import com.reverb.app.dto.responses.TokenResponse;
import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.User;
import com.reverb.app.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<GenericResponse>> register(@RequestBody RegisterRequest request) {
        return accountService.register(request.getEmail(), request.getName(), request.getPassword())
                .thenApply(result -> ResponseEntity.ok(
                        new GenericResponse("Success", "User created successfully")
                ))
                .exceptionally(ex -> ResponseEntity.badRequest().body(
                        new GenericResponse("Error", ex.getMessage())
                ));
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        return accountService.login(request.getEmail(), request.getPassword())
                .thenApply(result -> {
                    var user = (User) result.get("user");
                    return ResponseEntity.ok(
                            new LoginResponse(
                                    (String) result.get("accessToken"),
                                    (String) result.get("refreshToken"),
                                    new UserDto(
                                            user.getUserId(),
                                            user.getUserName(),
                                            user.getEmail(),
                                            user.getCreationDate(),
                                            user.getAvatar()
                                    )
                            )
                    );
                })
                .exceptionally(ex -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/refresh")
    public CompletableFuture<ResponseEntity<TokenResponse>> refreshToken(@RequestBody RefreshRequest request) {
        return accountService.refreshToken(request.getToken())
                .thenApply(tokens -> ResponseEntity.ok(
                        new TokenResponse(tokens.get("accessToken"), tokens.get("refreshToken"))
                ))
                .exceptionally(ex -> ResponseEntity.badRequest().body(null));
    }
}


