package com.reverb.app.controllers;

import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.User;
import com.reverb.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
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
}
