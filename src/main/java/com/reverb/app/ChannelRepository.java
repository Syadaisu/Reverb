package com.reverb.app;

import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, Integer> {
    Channel findByChannelName(String name);
}
