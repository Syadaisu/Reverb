// src/main/java/com/reverb/app/repositories/ServerRepository.java
package com.reverb.app.repositories;

import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServerRepository extends CrudRepository<Server, Integer> {
    Server findByServerName(String serverName);

    boolean existsByServerName(String serverName);

    Server findByIdAndOwnerId(int serverId, int ownerId);

    boolean existsByUserIdAndServerId(int userId, int serverId);

    boolean existsByOwnerIdAndServerId(int ownerId, int serverId);

    List<Server> findAllByUserId(int userId);

    List<User> findUsersByServerId(int serverId);

    void addUserToServer(int serverId, int userId);

    Server findByServerId(int serverId);

    void removeUserFromServer(int serverId, int userId);

    void deleteMessagesByServerId(int serverId);

    void deleteChannelsByServerId(int serverId);

    void deleteUserServersByServerId(int serverId);

    void updateServer(int serverId, String serverName, String description);
}