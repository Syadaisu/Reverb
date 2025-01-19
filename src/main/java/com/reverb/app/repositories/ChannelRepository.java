package com.reverb.app.repositories;

import com.reverb.app.models.Channel;
import com.reverb.app.models.Server;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelRepository extends CrudRepository<Channel, Integer> {
    Channel findByChannelName(String channelName);
    Channel findByChannelId(int channelId);
    List<Channel> findByServerServerId(int serverId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Channel c WHERE c.server.serverId = :serverId")
    void deleteAllByServerId(@Param("serverId") int serverId);
}
