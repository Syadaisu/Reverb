package com.reverb.app.controllers;


import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController  {
    public UserController(UserRepository userRepository) {this.userRepository=userRepository;}

    private final UserRepository userRepository;

    @GetMapping("/getAll")
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/getDummy")
    public Iterable<User> getDummy() {
        User user = new User();
        this.userRepository.save(user);
        return userRepository.findAll();
    }


    @PostMapping("/add")
    public User save(@RequestBody User user) {
        return userRepository.save(user);
    }

}
