package com.reverb.app.controllers;

import com.reverb.app.dto.requests.*;
import com.reverb.app.dto.responses.*;

import com.reverb.app.models.Channel;
import com.reverb.app.models.Message;
import com.reverb.app.models.MessageDocument;
import com.reverb.app.models.Server;
import com.reverb.app.services.ServerService;
import com.reverb.app.services.ChannelService;
import com.reverb.app.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.Map;

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


    /*@MessageMapping("/addMessage")
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
    }*/

    @MessageMapping("/addMessage")
    public void addMessage(AddMessageRequest payload) {
        System.out.println("WebSocketController.addMessage: " + payload
                + " channelId=" + payload.getChannelId()
                + " authorId=" + payload.getAuthorId()
                + " body=" + payload.getBody()
                + " responseToId=" + payload.getResponseToId()
                + " responseTo=" + payload.getResponseTo()
                + " attachmentUuid=" + payload.getAttachmentUuid());



        // 1) Persist the message in MongoDB
        MessageDocument saved = messageService.createMessageSync(
                payload.getChannelId(),
                payload.getAuthorId(),
                payload.getBody(),
                payload.getResponseToId(),
                payload.getResponseTo(),
                payload.getAttachmentUuid()
        );

        // 2) Build response DTO
        AddMessageResponse response = new AddMessageResponse();
        response.setMessageId(saved.getMessageId()); // MongoDB's ObjectId
        response.setChannelId(saved.getChannelId());
        response.setAuthorId(saved.getAuthorId());
        response.setBody(saved.getBody());
        response.setCreationDate(saved.getCreationDate());
        response.setDeleted(saved.getIsDeleted());
        response.setAttachmentUuid(saved.getAttachmentUuid());
        response.setResponseToId(saved.getResponseToId());
        response.setResponseTo(saved.getResponseTo());

        // 3) Broadcast to a dynamic path based on channelId
        String destination = "/topic/channel." + payload.getChannelId() + ".message.added";
        System.out.println("Broadcasting to destination=" + destination);

        messagingTemplate.convertAndSend(destination, response);
    }

    @MessageMapping("/editServerSignal")
    public void editServerSignal(Map<String, Object> payload) {
        // Suppose payload has "serverId"
        Object serverId = payload.get("serverId");

        // You do your normal REST/DB logic somewhere else (or in a service).
        // Then broadcast a minimal signal to a topic:
        messagingTemplate.convertAndSend(
                "/topic/server.edited", // topic name
                Collections.singletonMap("serverId", serverId)
        );
    }

    @MessageMapping("/editChannelSignal")
    public void editChannelSignal(Map<String, Object> payload) {
        // Suppose payload has "serverId"
        Object channelId = payload.get("channelId");

        // You do your normal REST/DB logic somewhere else (or in a service).
        // Then broadcast a minimal signal to a topic:
        messagingTemplate.convertAndSend(
                "/topic/channel.edited", // topic name
                Collections.singletonMap("channelId", channelId)
        );
    }

    @MessageMapping("/deleteChannelSignal")
    public void deleteChannelSignal(Map<String, Object> payload) {
        // Suppose payload has "channelId"
        Object channelId = payload.get("channelId");

        // After actually deleting the channel in DB,
        // broadcast a tiny message that "hey, channelId is deleted"
        messagingTemplate.convertAndSend(
                "/topic/channel.deleted",
                Collections.singletonMap("channelId", channelId)
        );
    }

    @MessageMapping("/deleteServerSignal")
    public void deleteServerSignal(Map<String, Object> payload) {
        // Suppose payload has "channelId"
        Object serverId = payload.get("serverId");

        // After actually deleting the channel in DB,
        // broadcast a tiny message that "hey, channelId is deleted"
        messagingTemplate.convertAndSend(
                "/topic/server.deleted",
                Collections.singletonMap("serverId", serverId)
        );
    }

    @MessageMapping("/editUserSignal")
    public void editUserSignal(Map<String, Object> payload) {
        // Suppose payload has "channelId"
        Object userId = payload.get("userId");

        // After actually deleting the channel in DB,
        // broadcast a tiny message that "hey, channelId is deleted"
        messagingTemplate.convertAndSend(
                "/topic/user.edited",
                Collections.singletonMap("userId", userId)
        );
    }

    @MessageMapping("/deleteMessageSignal")
    public void deleteMessageSignal(Map<String, Object> payload) {
        // Suppose payload has "channelId"
        Object messageId = payload.get("messageId");

        // After actually deleting the channel in DB,
        // broadcast a tiny message that "hey, channelId is deleted"
        messagingTemplate.convertAndSend(
                "/topic/message.deleted",
                Collections.singletonMap("messageId", messageId)
        );
    }
}
