package com.reverb.app.services;

import com.reverb.app.models.Attachment;
import com.reverb.app.models.MessageDocument;
import com.reverb.app.models.User;
import com.reverb.app.repositories.AttachmentRepository;
import com.reverb.app.repositories.MessageDocumentRepository;
import com.reverb.app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private MessageDocumentRepository messageDocumentRepository;

    @Autowired
    private UserRepository userRepository;

    public void uploadAvatar(int userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        // Create Avatar entity
        Attachment attachment = new Attachment(file.getBytes(), file.getContentType());
        System.out.println("AttachmentService.uploadAvatar: " + attachment.getAttachmentUuid() + " " + attachment.getContentType() + " " + attachment.getAttachmentData().length);

        attachmentRepository.save(attachment);

        // Set avatar to user
        user.setAvatar(attachment);

        // Save user (cascades to avatar)
        userRepository.save(user);
    }

    public Attachment getAttachmentByUuid(String attachmentUuid) {
        return attachmentRepository.findById(attachmentUuid)
                .orElseThrow(() -> new RuntimeException("Avatar not found with UUID=" + attachmentUuid));
    }


    public Attachment getAvatarForUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        return user.getAvatar(); // May return null if no avatar set
    }

    public String uploadFile(MultipartFile file) throws IOException {
        //User user = userRepository.findById()
                //.orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        // Create Attachment entity
        Attachment attachment = new Attachment(file.getBytes(), file.getContentType());
        System.out.println("AttachmentService.uploadMessageFile: " + attachment.getAttachmentUuid() + " " + attachment.getContentType() + " " + attachment.getAttachmentData().length);

        attachmentRepository.save(attachment);

        return attachment.getAttachmentUuid();
    }

    public void uploadMessageAttachment(String messageId, MultipartFile file) throws IOException {
        // Create Attachment entity
        Attachment attachment = new Attachment(file.getBytes(), file.getContentType());
        System.out.println("AttachmentService.uploadMessageAttachment: " + attachment.getAttachmentUuid() + " " + attachment.getContentType() + " " + attachment.getAttachmentData().length);

        attachmentRepository.save(attachment);

        // Set attachment to message
        MessageDocument messageDocument = messageDocumentRepository.findByMessageId(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with id=" + messageId));
        messageDocument.setAttachment(attachment.getAttachmentUuid());

        // Save message (cascades to attachment)
        messageDocumentRepository.save(messageDocument);
    }
}
