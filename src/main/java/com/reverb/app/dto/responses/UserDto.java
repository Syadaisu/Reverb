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
    private String avatarUuid;

    public UserDto(int userId, String userName, String email, Date creationDate, String avatarUuid) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.creationDate = creationDate;
        this.avatarUuid = avatarUuid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUuid() {
        return avatarUuid;
    }

    public void setAvatarUuid(String avatarUuid) {
        this.avatarUuid = avatarUuid;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}