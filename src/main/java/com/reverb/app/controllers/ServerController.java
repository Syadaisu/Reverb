package com.reverb.app.controllers;

import com.reverb.app.dto.requests.AddServerRequest;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import com.reverb.app.services.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/server")
public class ServerController {

    private final ServerService serverService;
    private final UserRepository userRepository;

    @Autowired
    public ServerController(ServerService serverService, UserRepository userRepository) {
        this.serverService = serverService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<GenericResponse>> addServer(@RequestBody AddServerRequest request, Principal principal) {

        String username = principal.getName();
        System.out.println("Username: " + username);
        User user = userRepository.findByUserName(username);
        int ownerId = user.getUserId();

        return serverService.addServer(request.getServerName(), request.getServerDescription(), ownerId)
                .thenApply(result -> ResponseEntity.ok(
                        new GenericResponse("Success", "Server created successfully")
                ))
                .exceptionally(ex -> ResponseEntity.badRequest().body(
                        new GenericResponse("Error", ex.getMessage())
                ));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getAll")
    public CompletableFuture<ResponseEntity<GenericResponse>> getAllServers() {
        return serverService.getAllServers()
                .thenApply(result -> ResponseEntity.ok(
                        new GenericResponse("Success", result.toString())
                ))
                .exceptionally(ex -> ResponseEntity.badRequest().body(
                        new GenericResponse("Error", ex.getMessage())
                ));
    }
}