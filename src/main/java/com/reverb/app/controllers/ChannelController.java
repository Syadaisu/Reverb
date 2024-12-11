package com.reverb.app.controllers;

import com.reverb.app.models.Channel;
import com.reverb.app.repositories.ChannelRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ChannelController {

    public ChannelController(ChannelRepository channelRepository) {this.channelRepository=channelRepository;}

    private final ChannelRepository channelRepository;

    @GetMapping("/getAll")
    public Iterable<Channel> getAll() {
        return channelRepository.findAll();
    }

    @GetMapping("/getDummy")
    public Iterable<Channel> getDummy() {
        Channel channel = new Channel();
        this.channelRepository.save(channel);
        return channelRepository.findAll();
    }


    @PostMapping("/add")
    public Channel save(@RequestBody Channel channel) {
        return channelRepository.save(channel);
    }
}
