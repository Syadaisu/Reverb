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
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
