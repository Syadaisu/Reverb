package com.reverb.app.services;

import com.reverb.app.models.File;
import com.reverb.app.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Optional<FileData> readBytes(UUID uuid) {
        Optional<File> fileOptional = Optional.ofNullable(fileRepository.findByFileId(uuid));
        if (fileOptional.isEmpty()) {
            return Optional.empty();
        }
        File file = fileOptional.get();
        return Optional.of(new FileData(file.getFileData(), file.getFileType()));
    }

    public UUID writeBytes(MultipartFile formFile) throws IOException {
        try (InputStream inputStream = formFile.getInputStream()) {
            byte[] data = inputStream.readAllBytes();
            String fileType = formFile.getContentType();
            File file = new File(data, fileType);
            fileRepository.save(file);
            return file.getFileId();
        }
    }

    public void delete(UUID uuid) {
        Optional<File> fileOptional = Optional.ofNullable(fileRepository.findByFileId(uuid));
        fileOptional.ifPresent(fileRepository::delete);
    }

    public static class FileData {
        private final byte[] data;
        private final String type;

        public FileData(byte[] data, String type) {
            this.data = data;
            this.type = type;
        }

        public byte[] getData() {
            return data;
        }

        public String getType() {
            return type;
        }
    }
}