// src/main/java/com/reverb/app/services/UserService.java

package com.reverb.app.services;

import com.reverb.app.dto.requests.EditUserRequest;
import com.reverb.app.models.Attachment;
import com.reverb.app.models.User;
import com.reverb.app.repositories.AttachmentRepository;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Edits the current user's username, password, and avatar (if provided).
     *
     * @param editUserRequest (userName, oldPassword, newPassword)
     * @param avatar optional new avatar
     * @throws Exception if oldPassword is invalid or if userName is taken
     */
    public void editUser(EditUserRequest editUserRequest, MultipartFile avatar) throws Exception {
        // 1) Identify the currently authenticated user
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // 2) Fetch that user from the DB
        User user = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        // 3) Update username if provided
        if (editUserRequest.getUserName() != null && !editUserRequest.getUserName().isBlank()) {
            // Check if new username is available
            Optional<User> existing = userRepository.findByUserName(editUserRequest.getUserName());
            if (existing.isPresent() && existing.get().getUserId() != user.getUserId()) {
                throw new Exception("Username is already taken.");
            }
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

        // 5) If avatar is provided, update or create attachment
        if (avatar != null && !avatar.isEmpty()) {
            Attachment attachment;
            if (user.getAvatar() != null) {
                // Reuse existing
                attachment = user.getAvatar();
            } else {
                attachment = new Attachment();
            }
            attachment.setAttachmentData(avatar.getBytes());
            attachment.setContentType(avatar.getContentType());
            attachmentRepository.save(attachment);  // Ensure the attachment is persisted
            user.setAvatar(attachment);
        }

        // 6) Save user
        userRepository.save(user);
    }
}
