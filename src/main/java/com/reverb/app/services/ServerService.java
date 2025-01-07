package com.reverb.app.services;

import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.ServerRepository;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository, UserRepository userRepository) {
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
    }

    // Add a new server
    public CompletableFuture<Server> addServer(String serverName, String description, int ownerId) {
        return CompletableFuture.supplyAsync(() -> {
            // Check if owner exists
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new UsernameNotFoundException("Owner not found"));

            Server server = new Server();
            server.setServerName(serverName);
            server.setDescription(description);
            server.setIsPublic(true);
            server.setOwnerId(ownerId);
            server.setAvatar(null);
            server.setOwner(owner); // Link the owner

            return serverRepository.save(server);
        });
    }

    // Delete a server
    public CompletableFuture<Void> deleteServer(int serverId, int ownerId) {
        return CompletableFuture.runAsync(() -> {
            Server server = serverRepository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Server not found"));

            // Ensure the owner is deleting their own server
            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to delete this server");
            }

            serverRepository.deleteById(serverId);
        });
    }

    // Get all servers for a user
    public CompletableFuture<List<Server>> getUserServers(int userId) {
        return CompletableFuture.supplyAsync(() -> serverRepository.findByOwnerId(userId));
    }

    public CompletableFuture<List<Server>> getAllServers() {
        return CompletableFuture.supplyAsync(() -> serverRepository.findAll());
    }
}