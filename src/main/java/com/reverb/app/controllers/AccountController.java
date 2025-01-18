package com.reverb.app.controllers;

import com.reverb.app.dto.requests.LoginRequest;
import com.reverb.app.dto.requests.RefreshRequest;
import com.reverb.app.dto.requests.RegisterRequest;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.dto.responses.LoginResponse;
import com.reverb.app.dto.responses.TokenResponse;
import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.Attachment;
import com.reverb.app.models.User;
import com.reverb.app.services.AccountService;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
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
        return accountService.register(request.getEmail(), request.getUserName(), request.getPassword())
                .thenApply(result -> ResponseEntity.ok(
                        new GenericResponse("Success", "User created successfully")
                ))
                .exceptionally(ex -> ResponseEntity.badRequest().body(
                        new GenericResponse("Error", ex.getMessage())
                ));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        System.out.println("loginControllerEnter");
        return accountService.login(request.getEmail(), request.getPassword())
                .thenApply(result -> {
                    System.out.println("ServiceCompleted");
                    var user = (User) result.get("user");
                    System.out.println(result);
                    String avatarUuid = (String) result.get("avatarUuid");
                    LoginResponse response = new LoginResponse(
                            (String) result.get("accessToken"),
                            (String) result.get("refreshToken"),
                            new UserDto(
                                    user.getUserId(),
                                    user.getUserName(),
                                    user.getEmail(),
                                    user.getCreationDate(),
                                    avatarUuid


                            )
                    );

                    System.out.println(response); // Print the response

                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                })
                .exceptionally(ex -> {
                    System.out.println("Error");
                    ex.printStackTrace();  // Optionally log the error
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new LoginResponse(null, null, null));
                });
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


