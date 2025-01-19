package com.reverb.app;

import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRepository() {
        User user = new User();
        user.setUserName("Test");
        user.setEmail("test@example.com");
        userRepository.save(user);
        assertTrue(userRepository.findById(user.getUserId()).isPresent());
    }
}