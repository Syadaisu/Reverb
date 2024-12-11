package com.reverb.app.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private byte[] avatar;

    @Column
    private Date creationDate;

    @Column
    private Boolean isRemoved;

    @ManyToMany
    @JoinTable(
            name = "user_server",  // The join table name
            joinColumns = @JoinColumn(name = "userId"),  // Foreign key to the User table
            inverseJoinColumns = @JoinColumn(name = "serverId")  // Foreign key to the Server table
    )
    private List<Server> servers;

    public User(){}

}
