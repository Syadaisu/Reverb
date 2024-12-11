package com.reverb.app;

import jakarta.persistence.*;

@Entity
@Table(name="MessageHandler")
public class MessageHandler {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int messageHandlerId;

    @Column
    private String contents;

    @Column
    private Integer channelId;

    @Column
    private String channel;

    @Column
    private String responseToId;

    @Column
    private String responseTo;
}
