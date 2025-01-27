package com.reverb.app.controllers;

import com.reverb.app.dto.requests.AddServerRequest;
import com.reverb.app.dto.requests.EditServerRequest;
import com.reverb.app.dto.responses.*;
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
import org.springframework.web.multipart.MultipartFile;

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
            User user = userRepository.findByUserId(ownerId)
                    .orElseThrow(() -> new Exception("Authenticated user not found"));

            if (user == null) {
                AddServerResponse errorResp = new AddServerResponse();
                errorResp.setErrorMessage("No user found for ID: " + ownerId);
                return ResponseEntity.badRequest().body(errorResp);
            }

            // 3. Create the server (blocking call if your service is async)
            Server savedServer = serverService.addServerSync(
                    request.getServerName(),
                    request.getServerDescription(),
                    ownerId
            );

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
                        server.getIsPublic(),
                        server.getOwnerId(),
                        server.getServerIconUuid()
                ))
                .collect(Collectors.toList());
        System.out.println("Mapped response: " + response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/delete/{serverId}")
    public ResponseEntity<GenericResponse> deleteServer(@PathVariable int serverId) {
        try {
            // 1. Get the authenticated user from SecurityContext
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            // 2. Invoke service method to delete the server
            serverService.deleteServer(serverId, ownerId).join();

            // 3. Return success response
            GenericResponse response = new GenericResponse("Success","Server deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            // Return an error response
            GenericResponse errorResp = new GenericResponse("Error","Failed to delete server: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResp);
        }
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getByUsers/{userId}")
    public ResponseEntity<List<ServerDto>> getServersByUsersId(@PathVariable int userId) {
        try {
            // 1. Await the async call
            List<ServerDto> serverDtos = serverService.getUserServers(userId).join();

            // 2. Return the results
            return ResponseEntity.ok(serverDtos);

        } catch (Exception ex) {
            // 3. Handle exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/edit/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServerDto> editServer(
            @PathVariable int serverId,
            @RequestBody EditServerRequest request
    ) {
        try {
            // 1. Get the authenticated user from SecurityContext
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            // 2. Call the async service to edit the server
            ServerDto updatedServerDto = serverService.editServer(
                    serverId,
                    ownerId,
                    request.getServerName(),
                    request.getDescription()
                    //request.getAvatar()
            ).join(); // This .join() will wait for the async call to finish.

            // 3. Return the updated server info
            return ResponseEntity.ok(updatedServerDto);

        } catch (Exception ex) {
            ex.printStackTrace();
            // You can return an error message, or a response object with the error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{serverId}")
    public ResponseEntity<ServerDto> getServerById(@PathVariable int serverId) {
        try {
            // Call the async service method and wait for completion
            ServerDto serverDto = serverService.getServerById(serverId).join();

            // Return the server info with status 200 (OK)
            return ResponseEntity.ok(serverDto);

        } catch (Exception ex) {
            // Handle errors (e.g., server not found)
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/join")
    public ResponseEntity<String> joinServer(
            @RequestParam String serverName,
            @RequestParam int userId
    ) {
        try {
            serverService.joinServer(serverName, userId);
            User user = userRepository.findByUserId(userId).orElseThrow(() -> new Exception("Authenticated user not found"));
            System.out.println("User's servers:" + user.getServers());
            return ResponseEntity.ok("User " + userId + " joined server: " + serverName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error joining server: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getByUser/{userId}")
    public ResponseEntity<List<ServerDto>> getServersByUserId(@PathVariable int userId) {
        try {
            List<ServerDto> dtos = serverService.getUserServersFromUserSide(userId);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/grantAdmin")
    public ResponseEntity<String> grantAdmin(
            @RequestParam String serverName,
            @RequestParam int userId
    ) {
        try {
            serverService.grantAuthority(serverName, userId);
            return ResponseEntity.ok("User " + userId + " granted admin on server " + serverName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error granting admin: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/grantAdminByEmail/{serverId}")
    public ResponseEntity<String> grantAdminByEmail(
            @PathVariable int serverId,
            @RequestParam String email
    ) {
        try {
            System.out.println("Granting admin to email: " + email);
            serverService.grantAuthorityByEmail(serverId, email);
            return ResponseEntity.ok("User with email " + email + " granted admin on server " + serverId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error granting admin: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/revokeAdmin")
    public ResponseEntity<String> revokeAdmin(
            @RequestParam String serverName,
            @RequestParam String email
    ) {
        try {
            serverService.revokeAuthority(serverName, email);
            return ResponseEntity.ok("User " + email + " revoked admin on server " + serverName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error revoking admin: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/avatar/{serverId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EditServerResponse> updateServer(
            @PathVariable int serverId,
            @RequestPart("avatar") MultipartFile avatar
    ) {
        try {
            serverService.updateServerAvatar(serverId,avatar);
            return ResponseEntity.ok(new EditServerResponse("Avatar updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new EditServerResponse("Error updating avatar: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/getServerAdminIds/{serverId}")
    public ResponseEntity<List<Integer>> getServerAdminIds(@PathVariable int serverId) {
        try {
            List<Integer> adminIds = serverService.getServerAdminIds(serverId);
            return ResponseEntity.ok(adminIds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }
}