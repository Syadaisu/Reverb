package com.reverb.app.repositories;

import com.reverb.app.models.Attachment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttachmentRepository extends CrudRepository<Attachment, String> {
    Attachment findByAttachmentUuid(String attachmentUuid);
}
