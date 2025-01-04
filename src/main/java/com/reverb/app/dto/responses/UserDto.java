package com.reverb.app.dto.responses;

import lombok.Data;

import java.util.Date;

@Data
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

}