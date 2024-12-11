package com.reverb.app.models;

import jakarta.persistence.*;

@Entity
@Table(name="MessageHandler")
public class MessageHandler {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int messageHandlerId;


    @ManyToOne
    @JoinColumn(name = "channelId", nullable = false)
    private Channel channel;

    @Column
    private String responseToId;

    @Column
    private String responseTo;
}
