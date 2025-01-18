package com.reverb.app.repositories;

import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUserName(String userName);
    boolean existsByEmail(String email);
    User findByUserId(int userId);
    Optional<User> findByEmail(String email);
}
