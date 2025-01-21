package com.reverb.app.controllers;

import com.reverb.app.dto.requests.*;
import com.reverb.app.dto.responses.*;

import com.reverb.app.models.Channel;
import com.reverb.app.models.Message;
import com.reverb.app.models.Server;
import com.reverb.app.services.ServerService;
import com.reverb.app.services.ChannelService;
import com.reverb.app.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/createServer")
    @SendTo("/topic/server.created")
    public AddServerResponse createServer(AddServerRequest payload) {
        System.out.println("WebSocketController.createServer: " + payload + " " + payload.getServerName() + " " + payload.getServerDescription() + " " + payload.getOwnerId());
        // 1) Save the new server in the DB or wherever
        Server server = serverService.addServerSync(
                payload.getServerName(),
                payload.getServerDescription(),
                payload.getOwnerId()
        );

        // 2) Return an event object that will be sent to ALL subscribers of "/topic/server.created"
        return new AddServerResponse(
                server.getServerId(),
                server.getServerName(),
                server.getDescription(),
                server.getIsPublic()// optional: if you want to broadcast the owner id
        );
    }

    @MessageMapping("/createChannel")
    @SendTo("/topic/server.channel.added")
    public AddChannelResponse addChannel(AddChannelRequest payload) {
        System.out.println("WebSocketController.addChannel: "+ payload + "serverId: " + payload.getServerId() + payload.getChannelName());
        // 1) Save the new channel in the DB or wherever
        Channel channel = channelService.createChannelSync(
                payload.getChannelName(),
                payload.getDescription(),
                payload.getServerId()
        );

        // 2) Return an event object that will be sent to ALL subscribers of "/topic/server.{serverId}.channel.added"
        return new AddChannelResponse(
                channel.getChannelId(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getRoleAccess()
        );
    }


    @MessageMapping("/addMessage")
    public void addMessage(AddMessageRequest payload) {
        System.out.println("WebSocketController.addMessage: " + payload
                + " channelId=" + payload.getChannelId()
                + " authorId=" + payload.getAuthorId()
                + " body=" + payload.getBody()
                + " responseToId=" + payload.getResponseToId()
                + " responseTo=" + payload.getResponseTo());

        // 1) Persist the message
        Message saved = messageService.createMessageSync(
                payload.getChannelId(),
                payload.getAuthorId(),
                payload.getBody(),
                payload.getResponseToId(),
                payload.getResponseTo()
        );

        // 2) Build response DTO
        AddMessageResponse response = new AddMessageResponse();
        response.setMessageId(saved.getMessageId());
        response.setChannelId(saved.getChannel().getChannelId());
        response.setAuthorId(saved.getAuthor().getUserId());
        response.setBody(saved.getBody());
        response.setCreationDate(saved.getCreationDate());
        response.setDeleted(false);
        response.setAttachment(saved.getAttachment());
        response.setResponseToId(saved.getResponseToId());
        response.setResponseTo(saved.getResponseTo());

        // 3) Broadcast to a dynamic path based on channelId
        // matching what your client is subscribing to, e.g. "/topic/channel.{channelId}.message.added"
        String destination = "/topic/channel." + payload.getChannelId() + ".message.added";
        System.out.println("Broadcasting to destination=" + destination);

        messagingTemplate.convertAndSend(destination, response);
    }
}
