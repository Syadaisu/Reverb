package com.reverb.app.models;


import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Server server;

    @Column
    private String description;


    public Channel() {
    }

    public Channel(String channelName, Server server, String description) {
        this.channelName = channelName;
        this.server = server;
        this.description = description;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
