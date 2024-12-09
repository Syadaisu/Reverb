package com.reverb.app;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

@Entity
@Table(name="Servers")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int serverId;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean isPublic;

    @Column
    private int ownerId;

    @Column
    private byte[] avatar;

    @ElementCollection
    private ArrayList<Integer> userIds;
}
