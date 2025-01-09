package com.reverb.app.controllers;

import com.reverb.app.dto.requests.AddServerRequest;
import com.reverb.app.dto.responses.AddServerResponse;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.dto.responses.ServerDto;
import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import com.reverb.app.services.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/server")
public class ServerController {

    private final ServerService serverService;
    private final UserRepository userRepository;
    private final SystemMetricsAutoConfiguration systemMetricsAutoConfiguration;

    @Autowired
    public ServerController(ServerService serverService, UserRepository userRepository, SystemMetricsAutoConfiguration systemMetricsAutoConfiguration) {
        this.serverService = serverService;
        this.userRepository = userRepository;
        this.systemMetricsAutoConfiguration = systemMetricsAutoConfiguration;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddServerResponse> addServer(
            @RequestBody AddServerRequest request
    ) {
        try {
            // 1. Grab the authenticated User object from SecurityContext
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();  // This is the actual DB userId

            System.out.println("Owner ID from principal: " + ownerId);
            System.out.println("Username from entity: " + authenticatedUser.getUserName());

            // 2. If needed, ensure the user actually exists in DB
            //    (optional if you trust the filter did that check)
            User user = userRepository.findByUserId(ownerId);
            if (user == null) {
                AddServerResponse errorResp = new AddServerResponse();
                errorResp.setErrorMessage("No user found for ID: " + ownerId);
                return ResponseEntity.badRequest().body(errorResp);
            }

            // 3. Create the server (blocking call if your service is async)
            Server savedServer = serverService.addServer(
                    request.getServerName(),
                    request.getServerDescription(),
                    ownerId
            ).join();

            // 4. Build and return the success response
            AddServerResponse successResp = new AddServerResponse(
                    savedServer.getServerId(),
                    savedServer.getServerName(),
                    savedServer.getDescription(),
                    savedServer.getIsPublic() != null ? savedServer.getIsPublic() : false
            );

            return ResponseEntity.ok(successResp);

        } catch (Exception ex) {
            // 5. Error handling
            AddServerResponse errorResp = new AddServerResponse();
            errorResp.setErrorMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(errorResp);
        }
    }


    /*@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<List<ServerDto>>> getAllServers() {
        System.out.println("getAllServers");
        return serverService.getAllServers()
                .thenApply(servers -> {
                    // Log and handle servers
                    System.out.println("Processing servers: " + servers);
                    System.out.println("SecurityContext inside thenApply: " + SecurityContextHolder.getContext().getAuthentication());
                    List<ServerDto> response = servers.stream()
                            .map(server -> new ServerDto(
                                    server.getServerId(),
                                    server.getServerName(),
                                    server.getDescription(),
                                    server.getIsPublic()
                            ))
                            .collect(Collectors.toList());
                    System.out.println("Mapped response: " + response);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(response);
                })
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        System.out.println("Completion exception: " + throwable.getMessage());
                        throwable.printStackTrace();
                    } else {
                        System.out.println("Response completed successfully.");
                    }
                })
                .exceptionally(ex -> {
                    System.out.println("Error in async flow: " + ex.getMessage());
                    ex.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(List.of());
                });

    }*/
    // Synchronous approach
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getAll")
    public ResponseEntity<List<ServerDto>> getAllServers() {
        System.out.println("getAllServers");
        // Wait (join) for the async service call to complete
        List<ServerDto> servers = serverService.getAllServers().join();

        // Convert or map your results
        List<ServerDto> response = servers.stream()
                .map(server -> new ServerDto(
                        server.getServerId(),
                        server.getServerName(),
                        server.getDescription(),
                        server.getIsPublic()
                ))
                .collect(Collectors.toList());
        System.out.println("Mapped response: " + response);
        return ResponseEntity.ok(response);
    }


}