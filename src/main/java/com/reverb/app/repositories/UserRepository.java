package com.reverb.app.repositories;

import com.reverb.app.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserId(int userId);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByUserNameAndPassword(String userName, String password);

    Optional<User> findByEmailAndUserName(String email, String userName);

    Optional<User> findByEmailAndUserNameAndPassword(String email, String userName, String password);

    Optional<User> findByEmailAndUserNameAndUserId(String email, String userName, int userId);

    Optional<User> findByEmailAndUserId(String email, int userId);

    Optional<User> findByUserNameAndUserId(String userName, int userId);

    Optional<User> findByEmailAndUserNameAndUserIdAndPassword(String email, String userName, int userId, String password);

}
