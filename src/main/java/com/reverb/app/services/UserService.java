package com.reverb.app.services;

import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async("securityAwareExecutor") // or remove if you don't have a custom executor
    public CompletableFuture<User> getByEmail(String email) {
        System.out.println("UserService.getByEmail: " + userRepository.findByEmail(email));
        return CompletableFuture.supplyAsync(() ->

                userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found by email: " + email))

        );
    }

    /*public void uploadAvatar(int userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        // Convert the multipart file to bytes
        byte[] avatarBytes = file.getBytes();

        // Set user avatar
        user.setAvatar(avatarBytes);

        // Save
        userRepository.save(user);
    }

    public byte[] getUserAvatar(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        return user.getAvatar(); // Could be null if no avatar
    }*/
}
