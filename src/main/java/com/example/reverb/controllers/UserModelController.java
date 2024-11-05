package com.example.reverb.controllers;

import com.example.reverb.models.UserModel;
import com.example.reverb.services.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/models")
public class UserModelController {

    @Autowired
    private UserModelService service;

    @GetMapping
    public List<UserModel> getAllModels() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<UserModel> getModelById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public UserModel createModel(@RequestBody UserModel model) {
        return service.save(model);
    }

    @DeleteMapping("/{id}")
    public void deleteModel(@PathVariable String id) {
        service.deleteById(id);
    }
}