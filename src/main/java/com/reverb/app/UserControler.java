package com.reverb.app;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControler  {
    public UserControler(UserRepository userRepository) {this.userRepository=userRepository;}

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
