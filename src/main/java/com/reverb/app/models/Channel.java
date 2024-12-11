package com.reverb.app.models;


import jakarta.persistence.*;

@Entity
@Table(name="Channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int channelId;

    @Column(nullable=false)
    private String channelName;

    @ManyToOne
    @JoinColumn(name="serverId",nullable=false)
    private Server server;

    @Column
    private String roleAccess;

    @Column
    private String description;

}
