package com.reverb.app.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.List;

@Entity
@Table(name="Servers")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "server_id")
    private int serverId;

    @Column(unique = true, nullable = false)
    private String serverName;

    @Column
    private String description;

    @Column(name="user_id",nullable=false)
    private Integer ownerId;

    @ManyToOne
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    @JsonIgnore
    private User owner;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_uuid", referencedColumnName = "attachment_uuid")
    private Attachment serverIcon;

    @ManyToMany(mappedBy = "servers")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> members;

    @ManyToMany
    @JoinTable(
            name = "server_authorized_users",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private List<User> authorizedUsers;

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setServerIcon(Attachment serverIcon) {
        this.serverIcon = serverIcon;
    }

    public Attachment getServerIcon() {
        return serverIcon;
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


    public int getOwnerId() {
        return owner.getUserId();
    }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }

    public List<User> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(List<User> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public void addAuthorizedUser(User user) {
        this.authorizedUsers.add(user);
        user.getAuthorizedServers().add(this);
    }

    public void removeAuthorizedUser(User user) {
        this.authorizedUsers.remove(user);
        user.getAuthorizedServers().remove(this);
    }
}
