package com.reverb.app.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public class AddFileRequest {

    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}