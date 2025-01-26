package com.reverb.app.repositories;

import com.reverb.app.models.Message;
import com.reverb.app.models.MessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String> {
    List<MessageDocument> findByChannelId(Integer channelId);
    Optional<MessageDocument> findByMessageId(String messageId);
}