package com.reverb.app.models;


import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

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

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(name = "userId", nullable = false)
    private Integer ownerId;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User owner;


    @Column
    private byte[] avatar;

    @ManyToMany(mappedBy = "servers")  // The "servers" field in User entity
    private List<User> members;

    public Server () {}

    public Server(String serverName, String description, Boolean isPublic, Integer ownerId) {
        this.serverName = serverName;
        this.description = description;
        this.isPublic = isPublic;
        this.ownerId = ownerId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public int getOwnerId() {
        return owner.getUserId();
    }

    public Boolean isPublic() {
        return isPublic;
    }
}