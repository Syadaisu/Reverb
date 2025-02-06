package com.reverb.app.controllers;

import com.reverb.app.dto.requests.*;
import com.reverb.app.dto.responses.*;
import com.reverb.app.models.Channel;
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
        //System.out.println("WebSocketController.createServer: " + payload + " " + payload.getServerName() + " "
                //+ payload.getServerDescription() + " " + payload.getOwnerId());
        Server server = serverService.addServerSync(
                payload.getServerName(),
                payload.getServerDescription(),
                payload.getOwnerId()
        );

        return new AddServerResponse(
                server.getServerId(),
                server.getServerName(),
                server.getDescription()
        );
    }

    @MessageMapping("/createChannel")
    @SendTo("/topic/server.channel.added")
    public AddChannelResponse addChannel(AddChannelRequest payload) {
        //System.out.println("WebSocketController.addChannel: "+ payload + "serverId: "
                //+ payload.getServerId() + payload.getChannelName());
        Channel channel = channelService.createChannelSync(
                payload.getChannelName(),
                payload.getDescription(),
                payload.getServerId()
        );

        return new AddChannelResponse(
                channel.getChannelId(),
                channel.getChannelName(),
                channel.getDescription()
        );
    }




    @MessageMapping("/addMessage")
    public void addMessage(AddMessageRequest payload) {
        /*System.out.println("WebSocketController.addMessage: " + payload
                + " channelId=" + payload.getChannelId()
                + " authorId=" + payload.getAuthorId()
                + " body=" + payload.getBody()
                + " responseToId=" + payload.getResponseToId()
                + " attachmentUuid=" + payload.getAttachmentUuid());*/

        MessageDocument saved = messageService.createMessageSync(
                payload.getChannelId(),
                payload.getAuthorId(),
                payload.getBody(),
                payload.getResponseToId(),
                payload.getAttachmentUuid()
        );

        AddMessageResponse response = new AddMessageResponse();
        response.setMessageId(saved.getMessageId());
        response.setChannelId(saved.getChannelId());
        response.setAuthorId(saved.getAuthorId());
        response.setBody(saved.getBody());
        response.setCreationDate(saved.getCreationDate());
        response.setAttachmentUuid(saved.getAttachmentUuid());
        response.setResponseToId(saved.getResponseToId());

        String destination = "/topic/channel." + payload.getChannelId() + ".message.added";
        //System.out.println("Broadcasting to destination=" + destination);

        messagingTemplate.convertAndSend(destination, response);
    }

    @MessageMapping("/editServerSignal")
    public void editServerSignal(Map<String, Object> payload) {
        Object serverId = payload.get("serverId");

        messagingTemplate.convertAndSend(
                "/topic/server.edited", // topic name
                Collections.singletonMap("serverId", serverId)
        );
    }

    @MessageMapping("/editChannelSignal")
    public void editChannelSignal(Map<String, Object> payload) {
        Object channelId = payload.get("channelId");

        messagingTemplate.convertAndSend(
                "/topic/channel.edited", // topic name
                Collections.singletonMap("channelId", channelId)
        );
    }

    @MessageMapping("/deleteChannelSignal")
    public void deleteChannelSignal(Map<String, Object> payload) {
        Object channelId = payload.get("channelId");

        messagingTemplate.convertAndSend(
                "/topic/channel.deleted",
                Collections.singletonMap("channelId", channelId)
        );
    }

    @MessageMapping("/deleteServerSignal")
    public void deleteServerSignal(Map<String, Object> payload) {
        Object serverId = payload.get("serverId");

        messagingTemplate.convertAndSend(
                "/topic/server.deleted",
                Collections.singletonMap("serverId", serverId)
        );
    }

    @MessageMapping("/editUserSignal")
    public void editUserSignal(Map<String, Object> payload) {
        Object userId = payload.get("userId");

        messagingTemplate.convertAndSend(
                "/topic/user.edited",
                Collections.singletonMap("userId", userId)
        );
    }

    @MessageMapping("/deleteMessageSignal")
    public void deleteMessageSignal(Map<String, Object> payload) {
        Object messageId = payload.get("messageId");

        messagingTemplate.convertAndSend(
                "/topic/message.deleted",
                Collections.singletonMap("messageId", messageId)
        );
    }
}
