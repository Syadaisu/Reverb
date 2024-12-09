package com.reverb.app;

import jakarta.persistence.*;

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


    public User(){}

}
