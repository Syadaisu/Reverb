package com.reverb.app.controllers;

import com.reverb.app.dto.requests.EditUserRequest;
import com.reverb.app.dto.responses.EditUserResponse;
import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.services.AttachmentService;
import com.reverb.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    @PutMapping(value = "/edit/{userId}")
    public ResponseEntity<EditUserResponse> editUser (
            @PathVariable int userId,
            @RequestBody EditUserRequest editUserRequest
    ) {
        if (editUserRequest == null) {
            editUserRequest = new EditUserRequest();
        }
        //System.out.println("Edit user controller" + editUserRequest.getUserName());
        try {
            userService.editUserData(userId,editUserRequest);
            //System.out.println("User updated successfully.");
            return ResponseEntity.ok(new EditUserResponse("User updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new EditUserResponse("Error updating user: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping(value = "/avatar/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EditUserResponse> updateAvatar(
            @PathVariable int userId,
            @RequestPart("avatar") MultipartFile avatar
    ) {
        try {
            userService.updateUserAvatar(userId,avatar);
            return ResponseEntity.ok(new EditUserResponse("Avatar updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new EditUserResponse("Error updating avatar: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(
            @PathVariable int userId
    ) {
        try {
            UserDto userDto = userService.getUserById(userId);
            //System.out.println("User found: " + userDto.getUserName());
            //System.out.println(ResponseEntity.ok(userDto));
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
