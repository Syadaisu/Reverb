package com.reverb.app;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="MessageContent")
public class MessageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer messageId;

    @Column
    private Integer authorId;

    @Column
    private Integer author;

    @Column
    private String body;

    @Column
    private Date creationDate;

    @Column
    private Boolean isDeleted;

    @Column
    private Integer attachment;

}
