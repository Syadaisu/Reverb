package com.reverb.app.repositories;

import com.reverb.app.models.Channel;
import com.reverb.app.models.Server;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChannelRepository extends CrudRepository<Channel, Integer> {
    Channel findByChannelName(String channelName);

    List<Channel> findByServerServerId(int serverId);
}
