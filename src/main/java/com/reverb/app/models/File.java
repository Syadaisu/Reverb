package com.reverb.app.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name="Files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID fileId;

    @Column(nullable=false)
    private String fileType;

    @Column(nullable=false)
    private byte[] fileData;


    public File(byte[] fileData, String fileType) {
        this.fileData = fileData;
        this.fileType = fileType;
    }

    public UUID getFileId() {
        return fileId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileType() {
        return fileType;
    }


}
