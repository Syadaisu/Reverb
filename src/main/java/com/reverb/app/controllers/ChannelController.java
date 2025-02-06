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

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addChannel(@RequestBody AddChannelRequest request) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            ChannelDto channelDto = channelService.createChannel(ownerId, request).join();

            return ResponseEntity.ok(channelDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            GenericResponse errorResp = new GenericResponse("Error","Error creating channel: " + ex.getMessage());
            return ResponseEntity.badRequest().body(errorResp);
        }
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getByServer/{serverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChannelDto>> getChannelsByServer(@PathVariable int serverId) {
        try {

            List<ChannelDto> channels = channelService.getChannelsByServer(serverId).join();

            return ResponseEntity.ok(channels);
        } catch (Exception ex) {
            ex.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }


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


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/edit/{channelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editChannel(@PathVariable int channelId,
                                         @RequestBody EditChannelRequest request) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            ChannelDto updatedChannelDto = channelService.editChannel(ownerId, channelId, request).join();

            return ResponseEntity.ok(updatedChannelDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            GenericResponse errorResp = new GenericResponse("Error","Error editing channel: " + ex.getMessage());
            return ResponseEntity.badRequest().body(errorResp);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable int channelId) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            int ownerId = authenticatedUser.getUserId();

            channelService.deleteChannel(ownerId, channelId).join();

            return ResponseEntity.ok(new GenericResponse("Success","Channel deleted successfully."));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(new GenericResponse("Error","Error deleting channel: " + ex.getMessage()));
        }
    }
}
