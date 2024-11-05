package com.example.reverb.repoes;

import com.example.reverb.models.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserModelRepository extends MongoRepository<UserModel, String> {
    // Custom query methods (if needed)
}