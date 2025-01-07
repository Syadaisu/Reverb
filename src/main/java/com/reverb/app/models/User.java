// User.java
package com.reverb.app.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
            name = "user_server",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "serverId")
    )
    private List<Server> servers;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "userId"))
    @Column(name = "authority")
    private List<String> authorities;

    public User() {
        this.authorities = Collections.singletonList("ROLE_USER");
    }

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

    @Override
    public String getUsername() {
        return "";
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

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}