// src/main/java/com/reverb/app/services/UserService.java

package com.reverb.app.services;

import com.reverb.app.dto.requests.EditUserRequest;
import com.reverb.app.dto.responses.ChannelDto;
import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.Attachment;
import com.reverb.app.models.Channel;
import com.reverb.app.models.User;
import com.reverb.app.repositories.AttachmentRepository;
import com.reverb.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void editUserData(int userId, EditUserRequest editUserRequest) throws Exception {
        // 1) Identify the currently authenticated user


        // 2) Fetch that user from the DB
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        System.out.println("Current username: " + user.getUserName());

        System.out.println("Editing user: " + user.getUserName() + " on " + editUserRequest.getUserName());
        // 3) Update username if provided
        if (editUserRequest.getUserName() != null && !editUserRequest.getUserName().isBlank()) {
            // Check if new username is available
            System.out.println("New username: " + editUserRequest.getUserName());
            user.setUserName(editUserRequest.getUserName());
        }

        // 4) If user wants to change password, oldPassword must be correct
        if (editUserRequest.getNewPassword() != null && !editUserRequest.getNewPassword().isBlank()) {
            // Ensure user provided the old password
            String oldPassword = editUserRequest.getOldPassword();
            if (oldPassword == null || oldPassword.isBlank()) {
                throw new Exception("Old password is required to change your password.");
            }

            // Check if old password matches
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new Exception("Old password is incorrect.");
            }

            // Now set the new password
            user.setPassword(passwordEncoder.encode(editUserRequest.getNewPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUserAvatar(int userId, MultipartFile avatar) throws Exception {
        // 1) Identify current user
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        // 2) If avatar is empty => maybe remove or throw error
        if (avatar == null || avatar.isEmpty()) {
            throw new Exception("No avatar file provided.");
        }

        // 3) If user has an existing attachment or not
        Attachment attachment;
        if (user.getAvatar() != null) {
            attachment = user.getAvatar();
        } else {
            attachment = new Attachment();
        }
        attachment.setAttachmentData(avatar.getBytes());
        attachment.setContentType(avatar.getContentType());
        attachmentRepository.save(attachment); // ensure stored

        user.setAvatar(attachment);
        userRepository.save(user);
    }

    @Transactional
    public UserDto getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        System.out.println("Data check " + user.getUserName() + " " + user.getEmail() + " " + user.getCreationDate() + " " + user.getAvatarUuid() + " " + user.getUserId());

        return new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreationDate(),
                user.getAvatarUuid()
        );
    }


}
