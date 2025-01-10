
package com.reverb.app.repositories;

import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServerRepository extends CrudRepository<Server, Integer> {
    Server findByServerName(String serverName);

    List<Server> findByOwnerId(int userId);

    List<Server> findAll();
}
