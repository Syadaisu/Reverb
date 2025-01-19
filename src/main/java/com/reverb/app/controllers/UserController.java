package com.reverb.app.controllers;

import com.reverb.app.dto.requests.EditUserRequest;
import com.reverb.app.dto.responses.EditUserResponse;
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

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/edit")
    public ResponseEntity<EditUserResponse> editUser(
            @RequestPart(value = "data", required = false) EditUserRequest editUserRequest,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        // If no JSON data is provided, create an empty request object
        if (editUserRequest == null) {
            editUserRequest = new EditUserRequest();
        }

        try {
            userService.editUser(editUserRequest, avatar);
            return ResponseEntity.ok(new EditUserResponse("User updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new EditUserResponse("Error updating user: " + e.getMessage()));
        }
    }
}
