package com.reverb.app.controllers;

import com.reverb.app.models.MessageHandler;
import com.reverb.app.repositories.MessageHandlerRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/msg")
public class MessageHandlerController {
    public MessageHandlerController(MessageHandlerRepository messageHandlerRepository) {this.messageHandlerRepository=messageHandlerRepository;}

    private final MessageHandlerRepository messageHandlerRepository;

    @GetMapping("/getAll")
    public Iterable<MessageHandler> getAll() {
        return messageHandlerRepository.findAll();
    }

    @GetMapping("/getDummy")
    public Iterable<MessageHandler> getDummy() {
        MessageHandler messageHandler = new MessageHandler();
        this.messageHandlerRepository.save(messageHandler);
        return messageHandlerRepository.findAll();
    }


    @PostMapping("/add")
    public MessageHandler save(@RequestBody MessageHandler messageHandler) {
        return messageHandlerRepository.save(messageHandler);
    }
}
