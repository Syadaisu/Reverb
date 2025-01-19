package com.reverb.app.repositories;

import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);
    Optional<User> findByUserId(int userId);
    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);



}
