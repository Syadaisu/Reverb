package com.reverb.app.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Attachments")
public class Attachment {

    @Id
    @Column(name = "attachment_uuid", updatable = false, nullable = false)
    private String attachmentUuid;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "attachment_data", nullable = false, columnDefinition = "bytea")
    private byte[] attachmentData;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    public Attachment() {
        this.attachmentUuid = UUID.randomUUID().toString();
    }

    public Attachment(byte[] avatarData, String contentType) {
        this.attachmentUuid = UUID.randomUUID().toString();
        this.attachmentData = avatarData;
        this.contentType = contentType;
        System.out.println("Attachment.constructor: " + this.attachmentUuid + " " + this.contentType + " " + this.attachmentData.length);
    }

    public String getAttachmentUuid() {
        return attachmentUuid;
    }

    public byte[] getAttachmentData() {
        return attachmentData;
    }

    public void setAttachmentData(byte[] attachmentData) {
        this.attachmentData = attachmentData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
