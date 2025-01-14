package com.reverb.app.controllers;

import com.reverb.app.dto.requests.*;
import com.reverb.app.dto.responses.*;
import com.reverb.app.models.Server;
import com.reverb.app.services.ServerService;
import com.reverb.app.services.ChannelService;
import com.reverb.app.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private ServerService serverService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageService messageService;


    @MessageMapping("/createServer")
    @SendTo("/topic/server.created")
    public AddServerResponse createServer(AddServerRequest payload) {
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

    // Example: Join a server
    /*@MessageMapping("/joinServer")
    @SendTo("/topic/server.{serverId}.joined")
    public ServerJoinedEvent joinServer(JoinServerPayload payload) {
        serverService.joinServer(payload.getServerId(), payload.getUserId());
        return new ServerJoinedEvent(payload.getServerId(), payload.getUserId());
    }

    // Example: Create a channel
    @MessageMapping("/createChannel")
    @SendTo("/topic/server.{serverId}.channel.created")
    public ChannelCreatedEvent createChannel(CreateChannelPayload payload) {
        Channel channel = channelService.createChannel(payload.getServerId(), payload.getName(), payload.getDescription());
        return new ChannelCreatedEvent(channel);
    }

    // Example: Edit a channel
    @MessageMapping("/editChannel")
    @SendTo("/topic/server.{serverId}.channel.edited")
    public ChannelEditedEvent editChannel(EditChannelPayload payload) {
        Channel channel = channelService.editChannel(payload.getChannelId(), payload.getName(), payload.getDescription());
        return new ChannelEditedEvent(channel);
    }

    // Example: Delete a channel
    @MessageMapping("/deleteChannel")
    @SendTo("/topic/server.{serverId}.channel.deleted")
    public ChannelDeletedEvent deleteChannel(DeleteChannelPayload payload) {
        channelService.deleteChannel(payload.getChannelId());
        return new ChannelDeletedEvent(payload.getChannelId());
    }

    // Example: Send a message
    @MessageMapping("/sendMessage")
    @SendTo("/topic/server.{serverId}.channel.{channelId}.messages")
    public MessageSentEvent sendMessage(SendMessagePayload payload) {
        Message message = messageService.sendMessage(payload.getServerId(), payload.getChannelId(), payload.getUserId(), payload.getContent());
        return new MessageSentEvent(message);
    }

    // Similarly, add more @MessageMapping methods as needed*/
}