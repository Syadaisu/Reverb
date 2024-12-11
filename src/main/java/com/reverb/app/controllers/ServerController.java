package com.reverb.app.controllers;

import com.reverb.app.models.Server;
import com.reverb.app.repositories.ServerRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/server")
public class ServerController {
    public ServerController(ServerRepository serverRepository) {this.serverRepository=serverRepository;}

    private final ServerRepository serverRepository;

    @GetMapping("/getAll")
    public Iterable<Server> getAll() {
        return serverRepository.findAll();
    }

    @GetMapping("/getDummy")
    public Iterable<Server> getDummy() {
        Server server = new Server();
        this.serverRepository.save(server);
        return serverRepository.findAll();
    }


    @PostMapping("/add")
    public Server save(@RequestBody Server server) {
        return serverRepository.save(server);
    }
}
