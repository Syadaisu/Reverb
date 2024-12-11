package com.reverb.app.repositories;

import com.reverb.app.models.Channel;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, Integer> {
    Channel findByChannelName(String channelName);
}
