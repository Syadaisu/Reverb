package com.reverb.app.services;

import com.reverb.app.dto.requests.AddMessageRequest;
import com.reverb.app.dto.requests.EditMessageRequest;
import com.reverb.app.dto.responses.MessageDto;
import com.reverb.app.models.Channel;
import com.reverb.app.models.Message;
import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.ChannelRepository;
import com.reverb.app.repositories.MessageRepository;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          ChannelRepository channelRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageDto createMessage(int ownerId, AddMessageRequest request) {
        // 1. Find channel
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new RuntimeException(
                        "Channel not found with ID: " + request.getChannelId()));

        // 2. Find user (author)
        User author = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with ID: " + ownerId));

        // 3. Create Message
        Message message = new Message();
        message.setChannel(channel);
        message.setAuthor(author);
        message.setBody(request.getBody());
        message.setCreationDate(new Date());
        message.setIsDeleted(false);
        message.setAttachment(null);
        message.setResponseToId(request.getResponseToId());
        message.setResponseTo(request.getResponseTo());

        // 4. Save
        message = messageRepository.save(message);

        // 5. Return as DTO
        return toMessageDto(message);
    }

    public Message createMessageSync(int channelId, int authorId, String body, String responsetoId, String responseto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new UsernameNotFoundException("Channel not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Message message = new Message();
        message.setChannel(channel);
        message.setBody(body);
        message.setCreationDate(new Date());
        message.setIsDeleted(false);
        message.setAttachment(0);
        message.setAuthor(author);
        message.setResponseToId(responsetoId);
        message.setResponseTo(responseto);
        // Additional setup if necessary
        return messageRepository.save(message);
    }

    public List<MessageDto> getMessagesByChannel(int channelId) {
        List<Message> messages = messageRepository.findByChannelChannelId(channelId);

        // Map all message entities to MessageDto
        return messages.stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDto editMessage(int userId, int messageId, EditMessageRequest request) {
        // 1. Find message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException(
                        "Message not found with ID: " + messageId));

        // 2. Check if user is the author (or if you want channel owner can also edit, adapt logic here)
        if (message.getAuthor().getUserId() != userId) {
            throw new RuntimeException("You do not have permission to edit this message.");
        }

        // 3. Update fields
        if (request.getBody() != null) {
            message.setBody(request.getBody());
        }
        if (request.getAttachment() != null) {
            message.setAttachment(request.getAttachment());
        }

        // Save
        message = messageRepository.save(message);

        return toMessageDto(message);
    }

    @Transactional
    public void deleteMessage(int userId, int messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException(
                        "Message not found with ID: " + messageId));

        // Only the author can delete (or you could check if user is channel/server owner)
        if (message.getAuthor().getUserId() != userId) {
            throw new RuntimeException("You do not have permission to delete this message.");
        }

        // Soft delete
        message.setIsDeleted(true);
        messageRepository.save(message);

        // Or if you want to truly remove from DB:
        // messageRepository.deleteById(messageId);
    }

    // Helper: Convert Message to MessageDto
    private MessageDto toMessageDto(Message msg) {
        return new MessageDto(
                msg.getMessageId(),
                msg.getChannel().getChannelId(),
                msg.getAuthor().getUserId(),
                msg.getBody(),
                msg.getCreationDate(),
                msg.getIsDeleted(),
                msg.getAttachment(),
                msg.getResponseToId(),
                msg.getResponseTo()
        );
    }
}
