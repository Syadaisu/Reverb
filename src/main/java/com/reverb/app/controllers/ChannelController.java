package com.reverb.app.controllers;

import com.reverb.app.dto.requests.AddChannelRequest;
import com.reverb.app.dto.requests.EditChannelRequest;
import com.reverb.app.dto.responses.ChannelDto;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.models.User;
import com.reverb.app.services.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;
    private final SystemMetricsAutoConfiguration systemMetricsAutoConfiguration;

    @Autowired
    public ChannelController(ChannelService channelService,
                             SystemMetricsAutoConfiguration systemMetricsAutoConfiguration) {
        this.channelService = channelService;
        this.systemMetricsAutoConfiguration = systemMetricsAutoConfiguration;
    }

    /**
     * Create a new channel for a server (only the server owner can).
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addChannel(@RequestBody AddChannelRequest request) {
        try {
            // 1. Get authenticated user
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            // 2. Create the channel
            ChannelDto channelDto = channelService.createChannel(ownerId, request).join();

            // 3. Return success
            return ResponseEntity.ok(channelDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Return error
            GenericResponse errorResp = new GenericResponse("Error","Error creating channel: " + ex.getMessage());
            return ResponseEntity.badRequest().body(errorResp);
        }
    }


    /**
     * Get all channels under a particular server.
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getByServer/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChannelDto>> getChannelsByServer(@PathVariable int serverId) {
        try {
            // 1. Call service
            List<ChannelDto> channels = channelService.getChannelsByServer(serverId).join();

            // 2. Return results
            return ResponseEntity.ok(channels);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Return empty list on error or a specialized error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }

    /**
     * Get a single channel by its ID
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/{channelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChannelDto> getChannelById(@PathVariable int channelId) {
        try {
            ChannelDto channelDto = channelService.getChannelById(channelId).join();
            return ResponseEntity.ok(channelDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Edit a channel’s name, roleAccess, and description.
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/edit/{channelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editChannel(@PathVariable int channelId,
                                         @RequestBody EditChannelRequest request) {
        try {
            // 1. Get authenticated user
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            // 2. Edit channel
            ChannelDto updatedChannelDto = channelService.editChannel(ownerId, channelId, request).join();

            // 3. Return success
            return ResponseEntity.ok(updatedChannelDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            GenericResponse errorResp = new GenericResponse("Error","Error editing channel: " + ex.getMessage());
            return ResponseEntity.badRequest().body(errorResp);
        }
    }

    /**
     * Delete a channel (only the server owner can).
     */
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable int channelId) {
        try {
            // 1. Get authenticated user
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            // 2. Delete
            channelService.deleteChannel(ownerId, channelId).join();

            // 3. Return success
            return ResponseEntity.ok(new GenericResponse("Success","Channel deleted successfully."));
        } catch (Exception ex) {
            ex.printStackTrace();
            // Return error
            return ResponseEntity.badRequest().body(new GenericResponse("Error","Error deleting channel: " + ex.getMessage()));
        }
    }
}
