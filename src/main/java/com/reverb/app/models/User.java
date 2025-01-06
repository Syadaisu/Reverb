package com.reverb.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Column
    private String userName;

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


    public void setCreatedAt(Date date) {
        this.creationDate = date;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }


    public String getEmail() {
        return email;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public User() {}

    public User(String userName, String password, String email, byte[] avatar, Date creationDate, Boolean isRemoved) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.avatar = avatar;
        this.creationDate = creationDate;
        this.isRemoved = isRemoved;
    }


}
