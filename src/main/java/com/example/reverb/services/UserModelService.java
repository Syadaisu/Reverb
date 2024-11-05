package com.example.reverb.services;

import com.example.reverb.models.UserModel;
import com.example.reverb.repoes.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserModelService {

    @Autowired
    private UserModelRepository repository;

    public List<UserModel> findAll() {
        return repository.findAll();
    }

    public Optional<UserModel> findById(String id) {
        return repository.findById(id);
    }

    public UserModel save(UserModel model) {
        return repository.save(model);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}