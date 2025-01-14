package com.reverb.app.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private int userId;
    private String userName;
    private String email;
    private Date creationDate;
    private byte[] avatar;

    public UserDto(int userId, String userName, String email, Date creationDate, byte[] avatar) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.creationDate = creationDate;
        this.avatar = avatar;
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
    public Date getCreationDate() {
        return creationDate;
    }
    public byte[] getAvatar() {
        return avatar;
    }
}