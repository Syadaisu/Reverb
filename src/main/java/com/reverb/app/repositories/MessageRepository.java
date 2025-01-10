package com.reverb.app.repositories;

import com.reverb.app.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Find all messages by channelId
    List<Message> findByChannelChannelId(int channelId);
}
