// src/main/java/com/reverb/app/models/Server.java
package com.reverb.app.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Servers")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "server_id")
    private int serverId;

    @Column(unique = true, nullable = false)
    private String serverName;

    @Column
    private String description;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(name="user_id",nullable=false)
    private Integer ownerId;

    @ManyToOne
    @JoinColumn(name="user_id", insertable = false, updatable = false)

    @JsonIgnore

    private User owner;

    @Column
    private byte[] avatar;

    @ManyToMany(mappedBy = "servers")
    @JsonIgnore// The "servers" field in User entity
    @OnDelete(action = OnDeleteAction.CASCADE) // Hibernate-specific annotation
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


    public int getServerId() {
        return serverId;
    }


    public String getServerName() {
        return serverName;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }


    public int getOwnerId() {
        return ownerId;
    }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }
}

