package com.reverb.app.services;

import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
}
