package com.reverb.app;


import jakarta.persistence.*;
import org.aspectj.lang.annotation.RequiredTypes;

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
    private Integer serverId;

    @Column
    private String server;

    @Column
    private int roleAccessId;

    @Column
    private String roleAccess;

    @Column
    private String description;

}
