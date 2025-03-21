package com.reverb.app.controllers;

import com.reverb.app.models.Attachment;
import com.reverb.app.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;


    @PostMapping(value = "/upload/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @PathVariable int userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            attachmentService.uploadAvatar(userId, file);
            return ResponseEntity.ok("Avatar uploaded successfully for user " + userId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading avatar: " + e.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/view/{attachmentUuid}")
    public ResponseEntity<byte[]> getAttachmentByUuid(@PathVariable String attachmentUuid) {
        try {
            Attachment avatar = attachmentService.getAttachmentByUuid(attachmentUuid);
            if (avatar.getAttachmentData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Determine content type
            MediaType mediaType = MediaType.parseMediaType(avatar.getContentType());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(avatar.getAttachmentData());

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<byte[]> getAvatarForUser(@PathVariable int userId) {
        try {
            Attachment avatar = attachmentService.getAvatarForUser(userId);
            if (avatar == null || avatar.getAttachmentData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Determine content type
            MediaType mediaType = MediaType.parseMediaType(avatar.getContentType());

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(avatar.getAttachmentData());

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
