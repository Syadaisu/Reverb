package com.reverb.app.services;

import com.reverb.app.dto.requests.AddMessageRequest;
import com.reverb.app.dto.requests.EditMessageRequest;
import com.reverb.app.dto.responses.MessageDocumentDto;
import com.reverb.app.dto.responses.MessageDto;
import com.reverb.app.models.*;
import com.reverb.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageDocumentRepository messageDocumentRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public MessageService(ChannelRepository channelRepository,
                          UserRepository userRepository,
                          MessageDocumentRepository messageDocumentRepository,
                          AttachmentRepository attachmentRepository) {
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
        this.messageDocumentRepository = messageDocumentRepository;
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Create a new message in MongoDB
     */
    @Transactional
    public MessageDocumentDto createMessage(int authorId, AddMessageRequest request) {
        // 1. Find channel
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new RuntimeException(
                        "Channel not found with ID: " + request.getChannelId()));

        // 2. Verify author exists
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with ID: " + authorId));


        // 3. Create MessageDocument
        MessageDocument messageDoc = new MessageDocument(
                request.getChannelId(),
                authorId,
                request.getBody(),
                new Date(),
                false,
                request.getAttachmentUuid(),
                request.getResponseToId(),
                request.getResponseTo()
        );

        // 4. Save to MongoDB
        MessageDocument savedDoc = messageDocumentRepository.save(messageDoc);

        // 5. Return as DTO
        return toMessageDocumentDto(savedDoc);
    }

    public MessageDocumentDto createAttachmentMessage(int channelId, int authorId, MultipartFile file){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attachment attachment = new Attachment();
        try {
            attachment.setAttachmentData(file.getBytes());
            attachment.setContentType(file.getContentType());
        } catch (Exception e) {
            throw new RuntimeException("Error uploading attachment: " + e.getMessage());
        }

        // Save attachment
        attachment = attachmentRepository.save(attachment);

        // Create MessageDocument
        MessageDocument messageDoc = new MessageDocument(
                channelId,
                authorId,
                "",
                new Date(),
                false,
                attachment.getAttachmentUuid(),
                "",
                ""
        );

        // Save to MongoDB
        MessageDocument savedDoc = messageDocumentRepository.save(messageDoc);

        return toMessageDocumentDto(savedDoc);
    }

    /**
     * Synchronous creation for WebSocket
     */
    public MessageDocument createMessageSync(int channelId, int authorId, String body, String responsetoId, String responseto, String Attachment) {
        // Find channel
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        // Find author
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create MessageDocument
        MessageDocument messageDoc = new MessageDocument(
                channelId,
                authorId,
                body,
                new Date(),
                false,
                Attachment, // Assuming default attachment value
                responsetoId,
                responseto
        );

        // Save to MongoDB
        return messageDocumentRepository.save(messageDoc);
    }

    /**
     * Retrieve messages by channel from MongoDB
     */
    public List<MessageDocumentDto> getMessagesByChannel(int channelId) {
        List<MessageDocument> messageDocs = messageDocumentRepository.findByChannelId(channelId);

        // Convert to MessageDto
        return messageDocs.stream()
                .map(this::toMessageDocumentDto)
                .collect(Collectors.toList());
    }

    /**
     * Edit a message in MongoDB
     */
    @Transactional
    public MessageDocumentDto editMessage(int userId, String messageId, EditMessageRequest request) {
        // 1. Find message
        Optional<MessageDocument> optionalMsgDoc = messageDocumentRepository.findById(messageId);
        if (!optionalMsgDoc.isPresent()) {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }

        MessageDocument messageDoc = optionalMsgDoc.get();

        // 2. Check if user is the author
        if (!Objects.equals(messageDoc.getAuthorId(), userId)) {
            throw new RuntimeException("You do not have permission to edit this message.");
        }

        // 3. Update fields
        if (request.getBody() != null) {
            messageDoc.setBody(request.getBody());
        }
        if (request.getAttachmentUuid() != null) {
            messageDoc.setAttachment(request.getAttachmentUuid());
        }

        // 4. Save
        messageDocumentRepository.save(messageDoc);

        return toMessageDocumentDto(messageDoc);
    }

    /**
     * Delete (soft-delete) a message in MongoDB
     */
    @Transactional
    public void deleteMessage(int userId, String messageId) {
        Optional<MessageDocument> optionalMsgDoc = messageDocumentRepository.findById(messageId);
        if (!optionalMsgDoc.isPresent()) {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }

        MessageDocument messageDoc = optionalMsgDoc.get();

        // Only the author can delete
        if (!Objects.equals(messageDoc.getAuthorId(), userId)) {
            throw new RuntimeException("You do not have permission to delete this message.");
        }

        // Soft delete
        messageDoc.setIsDeleted(true);
        messageDocumentRepository.save(messageDoc);
    }

    /**
     * Helper: Convert MessageDocument to MessageDto
     */
    private MessageDocumentDto toMessageDocumentDto(MessageDocument msgDoc) {
        return new MessageDocumentDto(
                msgDoc.getMessageId(), // Using MongoDB's ObjectId as messageId
                msgDoc.getChannelId(),
                msgDoc.getAuthorId(),
                msgDoc.getBody(),
                msgDoc.getCreationDate(),
                msgDoc.getIsDeleted(),
                msgDoc.getAttachmentUuid(),
                msgDoc.getResponseToId(),
                msgDoc.getResponseTo()
        );
    }
}



    /*@Autowired
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
    }*/

