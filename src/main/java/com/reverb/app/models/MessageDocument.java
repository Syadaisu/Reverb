package com.reverb.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "messages")
public class MessageDocument {

    @Id
    private String messageId; // MongoDB's ObjectId as a string
    private Integer channelId; // Reference to the channel in RDBMS
    private Integer authorId;  // Reference to the user in RDBMS
    private String body;
    private Date creationDate;
    private Boolean isDeleted;
    private Integer attachment;
    private String responseToId;
    private String responseTo;

    // Constructors
    public MessageDocument() {}

    public MessageDocument(Integer channelId, Integer authorId, String body, Date creationDate, Boolean isDeleted, Integer attachment, String responseToId, String responseTo) {
        this.channelId = channelId;
        this.authorId = authorId;
        this.body = body;
        this.creationDate = creationDate;
        this.isDeleted = isDeleted;
        this.attachment = attachment;
        this.responseToId = responseToId;
        this.responseTo = responseTo;
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getAttachment() {
        return attachment;
    }

    public void setAttachment(Integer attachment) {
        this.attachment = attachment;
    }

    public String getResponseToId() {
        return responseToId;
    }

    public void setResponseToId(String responseToId) {
        this.responseToId = responseToId;
    }

    public String getResponseTo() {
        return responseTo;
    }

    public void setResponseTo(String responseTo) {
        this.responseTo = responseTo;
    }


}
