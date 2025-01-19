package com.reverb.app.dto.requests;

public class EditUserRequest {

    private String userName;      // Optional new username
    private String oldPassword;   // Current password (for verification)
    private String newPassword;   // Optional new password

    public EditUserRequest() {
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
