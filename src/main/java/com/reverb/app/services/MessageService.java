package com.reverb.app.services;

import com.reverb.app.dto.requests.AddMessageRequest;
import com.reverb.app.dto.requests.EditMessageRequest;
import com.reverb.app.dto.responses.MessageDocumentDto;
import com.reverb.app.models.*;
import com.reverb.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public MessageDocumentDto createMessage(int authorId, AddMessageRequest request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new RuntimeException(
                        "Channel not found with ID: " + request.getChannelId()));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with ID: " + authorId));

        MessageDocument messageDoc = new MessageDocument(
                request.getChannelId(),
                authorId,
                request.getBody(),
                new Date(),
                request.getAttachmentUuid(),
                request.getResponseToId()
        );

        MessageDocument savedDoc = messageDocumentRepository.save(messageDoc);

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

        attachment = attachmentRepository.save(attachment);

        MessageDocument messageDoc = new MessageDocument(
                channelId,
                authorId,
                "",
                new Date(),
                attachment.getAttachmentUuid(),
                ""
        );

        MessageDocument savedDoc = messageDocumentRepository.save(messageDoc);

        return toMessageDocumentDto(savedDoc);
    }

    public MessageDocument createMessageSync(int channelId, int authorId, String body, String responsetoId, String Attachment) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        MessageDocument messageDoc = new MessageDocument(
                channelId,
                authorId,
                body,
                new Date(),
                Attachment, // Assuming default attachment value
                responsetoId
        );

        return messageDocumentRepository.save(messageDoc);
    }

    public List<MessageDocumentDto> getMessagesByChannel(int channelId) {
        List<MessageDocument> messageDocs = messageDocumentRepository.findByChannelId(channelId);

        // Convert to MessageDto
        return messageDocs.stream()
                .map(this::toMessageDocumentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDocumentDto editMessage(int userId, String messageId, EditMessageRequest request) {
        Optional<MessageDocument> optionalMsgDoc = messageDocumentRepository.findById(messageId);
        if (!optionalMsgDoc.isPresent()) {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }

        MessageDocument messageDoc = optionalMsgDoc.get();

        if (!Objects.equals(messageDoc.getAuthorId(), userId)) {
            throw new RuntimeException("You do not have permission to edit this message.");
        }

        if (request.getBody() != null) {
            messageDoc.setBody(request.getBody());
        }
        if (request.getAttachmentUuid() != null) {
            messageDoc.setAttachment(request.getAttachmentUuid());
        }

        messageDocumentRepository.save(messageDoc);

        return toMessageDocumentDto(messageDoc);
    }

    @Transactional
    public void deleteMessage(int userId, String messageId) {
        Optional<MessageDocument> optionalMsgDoc = messageDocumentRepository.findById(messageId);
        if (!optionalMsgDoc.isPresent()) {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }

        MessageDocument messageDoc = optionalMsgDoc.get();
        System.out.println(messageDoc.getAuthorId());
        // Only the author can delete
        //if (!Objects.equals(messageDoc.getAuthorId(), userId)) {
            //throw new RuntimeException("You do not have permission to delete this message.");
       // }

        messageDocumentRepository.deleteById(messageId);
        messageDocumentRepository.save(messageDoc);
    }

    private MessageDocumentDto toMessageDocumentDto(MessageDocument msgDoc) {
        return new MessageDocumentDto(
                msgDoc.getMessageId(),
                msgDoc.getChannelId(),
                msgDoc.getAuthorId(),
                msgDoc.getBody(),
                msgDoc.getCreationDate(),
                msgDoc.getAttachmentUuid(),
                msgDoc.getResponseToId()
        );
    }
}


