package com.reverb.app;

import org.springframework.data.repository.CrudRepository;

public interface MessageHandlerRepository extends CrudRepository<MessageHandler, Integer> {
    MessageHandler findByMessageId(Integer messageId);
}
