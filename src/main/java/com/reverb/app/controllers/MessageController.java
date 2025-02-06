package com.reverb.app.controllers;

import com.reverb.app.dto.requests.AddMessageRequest;
import com.reverb.app.dto.requests.EditMessageRequest;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.dto.responses.MessageDocumentDto;
import com.reverb.app.models.User;
import com.reverb.app.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final SystemMetricsAutoConfiguration systemMetricsAutoConfiguration;

    @Autowired
    public MessageController(MessageService messageService,
                             SystemMetricsAutoConfiguration systemMetricsAutoConfiguration) {
        this.messageService = messageService;
        this.systemMetricsAutoConfiguration = systemMetricsAutoConfiguration;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMessage(@RequestBody AddMessageRequest request) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            int userId = user.getUserId();

            MessageDocumentDto createdMessage = messageService.createMessage(userId, request);

            return ResponseEntity.ok(createdMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new GenericResponse("Error", "Error creating message: " + ex.getMessage())
            );
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/addAttachmentMessage/{channelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAttachmentMessage(@PathVariable int channelId,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            int userId = user.getUserId();

            MessageDocumentDto createdMessage = messageService.createAttachmentMessage(channelId,userId, file);

            return ResponseEntity.ok(createdMessage);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new GenericResponse("Error", "Error creating message: " + ex.getMessage())
            );
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getByChannel/{channelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDocumentDto>> getMessagesByChannel(@PathVariable int channelId) {
        try {
            List<MessageDocumentDto> messages = messageService.getMessagesByChannel(channelId);
            return ResponseEntity.ok(messages);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/edit/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editMessage(@PathVariable String messageId, // Changed to String for MongoDB ObjectId
                                         @RequestBody EditMessageRequest request) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            MessageDocumentDto updatedMessage = messageService.editMessage(user.getUserId(), messageId, request);
            return ResponseEntity.ok(updatedMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new GenericResponse("Error", "Error editing message: " + ex.getMessage())
            );
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable String messageId) { // Changed to String for MongoDB ObjectId
        try {
            // Authenticated user
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            messageService.deleteMessage(user.getUserId(), messageId);

            return ResponseEntity.ok(new GenericResponse("Success", "Message deleted successfully."));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(
                    new GenericResponse("Error", "Error deleting message: " + ex.getMessage())
            );
        }
    }
}