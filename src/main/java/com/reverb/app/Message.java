package com.reverb.app;

import jakarta.persistence.*;

@Entity
@Table(name="Messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int messageId;

    @Column
    private String contents;

    @Column
    private String channelId;

    @Column
    private String channel;

    @Column
    private String responseToId;

    @Column
    private String responseTo;
}
