package com.reverb.app.controllers;

import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.User;
import com.reverb.app.services.AttachmentService;
import com.reverb.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getByEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        try {
            // If getByEmail is async, we can .join() to wait for the result
            User user = userService.getByEmail(email).join();

            // Map entity to DTO
            UserDto userDto = new UserDto(
                    user.getUserId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getCreationDate(),
                    user.getAvatar()
            );
            System.out.println("UserController.getUserByEmail: " + userDto);
            return ResponseEntity.ok(userDto);

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    // 1) Upload
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/{userId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @PathVariable int userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            attachmentService.uploadAvatar(userId, file);
            return ResponseEntity.ok("Avatar uploaded for user " + userId);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading avatar: " + e.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 2) Download
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{userId}/avatar")
    public ResponseEntity<?> getAvatar(@PathVariable int userId) {
        Attachment avatarFile = attachmentService.getAvatarForUser(userId);
        if (avatarFile == null) {
            return ResponseEntity.notFound().build();
        }

        // We can return bytes or we can use a Resource.
        // For simplicity, let's do bytes.
        // If you want dynamic content-type detection, do a Tika or guess from extension. We'll assume "image/png" for example:

        try {
            byte[] bytes = Files.readAllBytes(avatarFile.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // or detect
                    .body(bytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Could not read avatar file");
        }
    }*/
}
