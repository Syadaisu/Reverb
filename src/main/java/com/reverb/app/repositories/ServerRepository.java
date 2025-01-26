package com.reverb.app.repositories;

import com.reverb.app.models.Server;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends CrudRepository<Server, Integer> {
    Optional<Server> findByServerName(String serverName);

    List<Server> findByOwnerId(int userId);

    @Query("SELECT s FROM Server s JOIN s.members m WHERE m.userId = :userId")
    List<Server> findAllByMemberUserId(@Param("userId") int userId);

    List<Server> findAll();

    Optional<Server> findByServerId(int serverId);


}
