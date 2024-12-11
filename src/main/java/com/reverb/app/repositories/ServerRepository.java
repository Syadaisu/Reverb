package com.reverb.app.repositories;

import com.reverb.app.models.Server;
import org.springframework.data.repository.CrudRepository;

public interface ServerRepository extends CrudRepository<Server, Integer> {
    Server findByServerName(String serverName);
}
