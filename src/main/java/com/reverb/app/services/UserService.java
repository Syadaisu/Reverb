package com.reverb.app.services;

import com.reverb.app.dto.requests.EditUserRequest;
import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.Attachment;
import com.reverb.app.models.User;
import com.reverb.app.repositories.AttachmentRepository;
import com.reverb.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        //System.out.println("Current username: " + user.getUserName());

        //System.out.println("Editing user: " + user.getUserName() + " on " + editUserRequest.getUserName());
        if (editUserRequest.getUserName() != null && !editUserRequest.getUserName().isBlank()) {
            //System.out.println("New username: " + editUserRequest.getUserName());
            user.setUserName(editUserRequest.getUserName());
        }

        if (editUserRequest.getNewPassword() != null && !editUserRequest.getNewPassword().isBlank()) {
            String oldPassword = editUserRequest.getOldPassword();
            if (oldPassword == null || oldPassword.isBlank()) {
                throw new Exception("Old password is required to change your password.");
            }

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new Exception("Old password is incorrect.");
            }
            user.setPassword(passwordEncoder.encode(editUserRequest.getNewPassword()));
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUserAvatar(int userId, MultipartFile avatar) throws Exception {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        if (avatar == null || avatar.isEmpty()) {
            throw new Exception("No avatar file provided.");
        }

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

        //System.out.println("Data check " + user.getUserName() + " " + user.getEmail() + " " + user.getCreationDate() + " " + user.getAvatarUuid() + " " + user.getUserId());

        return new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreationDate(),
                user.getAvatarUuid()
        );
    }


}
