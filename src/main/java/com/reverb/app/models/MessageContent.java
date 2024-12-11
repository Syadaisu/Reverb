package com.reverb.app.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="MessageContent")
public class MessageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer messageHandlerId;

    @Column
    private Integer authorId;

    @ManyToOne
    @JoinColumn(name = "userId",nullable=false)
    private User author;

    @Column(nullable=false)
    private String body;

    @Column(nullable=false)
    private Date creationDate;

    @Column(nullable=false)
    private Boolean isDeleted;

    @Column(nullable=false)
    private Integer attachment;

}
