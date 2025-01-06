package com.reverb.app.services;

import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import com.reverb.app.dto.requests.UserEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final DatabaseService apiDbContext;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(DatabaseService apiDbContext, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.apiDbContext = apiDbContext;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Optional<User> getUserQuery(int userId, int otherUserId) {
        return userRepository.findById(otherUserId);
    }

    public Optional<User> findUserIfReachable(int userId, int otherUserId) {
        return getUserQuery(userId, otherUserId);
    }

    public Optional<User> findUserIfNotBlocked(int userId, int otherUserId) {
        return getUserQuery(userId, otherUserId);
    }

    public User getUser(User user) {
        return getUserById(getUserId(user));
    }

    public boolean editUser(User user, UserEditRequest request, UUID pictureUUID) {
        boolean res = editUser(user, request.getEmail(), request.getUserName(), request.getNewPassword(), request.getPassword(), pictureUUID);
        if (res) {
            apiDbContext.saveChanges();
        }
        return res;
    }

    private boolean editUser(User user, String email, String userName, String newPassword, String password, UUID pictureUUID) {
        User existingUser = getUser(user);
        if (email != null) {
            existingUser.setEmail(email);
        }
        if (userName != null) {
            existingUser.setUserName(userName);
        }
        if (newPassword != null && password != null) {
            if (passwordEncoder.matches(password, existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                return false;
            }
        }
        if (pictureUUID != null) {
            // existingUser.setAvatar(pictureUUID);
        }

        userRepository.save(existingUser);
        return true;
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getUserByUsername(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean userExists(User user) {
        int userId = user.getUserId();
        return userRepository.existsById(userId);
    }

    public void ensureUserExists(User user) {
        if (!userExists(user)) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public int getUserId(User user) {
        return user.getUserId();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}