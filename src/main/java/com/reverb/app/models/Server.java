package com.reverb.app.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Servers")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int serverId;

    @Column
    private String serverName;

    @Column
    private String description;

    @Column(nullable=false)
    private Boolean isPublic;

    @Column(name="userId",nullable=false)
    private Integer ownerId;

    @ManyToOne
    @JoinColumn(name="userId", insertable = false, updatable = false)
    private User owner;


    @Column
    private byte[] avatar;

    @ManyToMany(mappedBy = "servers")  // The "servers" field in User entity
    private List<User> members;

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }


    public int getOwnerId() {
        return ownerId;
    }
}
