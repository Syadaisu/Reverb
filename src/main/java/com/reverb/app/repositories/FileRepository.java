package com.reverb.app.repositories;

import com.reverb.app.models.File;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FileRepository extends CrudRepository<File, Integer> {
    File findByFileId(UUID fileId);
}
