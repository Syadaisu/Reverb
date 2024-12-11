package com.reverb.app.repositories;

import com.reverb.app.models.MessageHandler;
import org.springframework.data.repository.CrudRepository;

public interface MessageHandlerRepository extends CrudRepository<MessageHandler, Integer> {
    MessageHandler findByMessageHandlerId(Integer messageHandlerId);
}
