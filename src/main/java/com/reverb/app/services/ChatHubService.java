package com.reverb.app.services;

import com.reverb.app.dto.responses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatHubService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(ChatHubService.class);

    public ChatHubService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(int serverId, MessageResponse message) {
        logger.info("User sent message {} to server {}", message.getMessageId(), serverId);
        messagingTemplate.convertAndSend("/topic/server-" + serverId, message);
    }

    public void deleteMessage(int serverId, int messageId) {
        logger.info("User deleted message {} from server {}", messageId, serverId);
        messagingTemplate.convertAndSend("/topic/server-" + serverId, new DeleteMessageResponse(messageId));
    }

    public void createChannel(int serverId, ChannelResponse channel) {
        logger.info("User created channel {} in server {}", channel.getChannelId(), serverId);
        messagingTemplate.convertAndSend("/topic/server-" + serverId, channel);
    }

    public void deleteChannel(int serverId, int channelId) {
        logger.info("User deleted channel {} from server {}", channelId, serverId);
        messagingTemplate.convertAndSend("/topic/server-" + serverId, new DeleteChannelResponse(channelId));
    }

    public void editChannel(int serverId, int channelId, String name, String description) {
        logger.info("User edited channel {} in server {}", channelId, serverId);
        messagingTemplate.convertAndSend("/topic/server-" + serverId, new EditChannelResponse(channelId, name, description, serverId));
    }

    public void editServer(int serverId, String name, String description) {
        logger.info("User edited server {}", serverId);
        messagingTemplate.convertAndSend("/topic/server-" + serverId, new EditServerResponse(serverId, name, description));
    }

    public void sendPrivateMessage(String userId, String friendId, PrivateMessageResponse message) {
        logger.info("User sent message {} to friend {}", message.getMessageId(), friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Cannot send message to self");
        }

        String group = userId.compareTo(friendId) < 0 ? userId + "-" + friendId : friendId + "-" + userId;
        messagingTemplate.convertAndSend("/queue/" + group, message);
    }

    public void deletePrivateMessage(String userId, String friendId, int messageId) {
        logger.info("User deleted message {} from friend {}", messageId, friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Cannot delete message from self");
        }

        String group = userId.compareTo(friendId) < 0 ? userId + "-" + friendId : friendId + "-" + userId;
        messagingTemplate.convertAndSend("/queue/" + group, new DeleteMessageResponse(messageId));
    }
}