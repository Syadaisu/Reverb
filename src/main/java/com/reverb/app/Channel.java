package com.reverb.app;


import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name="Channels")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int channelId;

    @Column
    private String name;

    @Column
    private String serverId;

    @Column
    private String server;

    @Column
    private String RoleAccessId;

    @Column
    private String RoleAccess;

    @Column
    private String description;

}
