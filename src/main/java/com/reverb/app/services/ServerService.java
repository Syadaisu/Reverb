package com.reverb.app.services;

import com.reverb.app.dto.responses.ServerDto;
import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.ServerRepository;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository, UserRepository userRepository) {
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
    }


    @Async("securityAwareExecutor")
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

    @Transactional
    public Server addServerSync(String name, String description, int ownerId) {
        // 1) Fetch the owner
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UsernameNotFoundException("Owner not found"));

        // 2) Create the server object
        Server server = new Server();
        server.setServerName(name);
        server.setDescription(description);
        server.setIsPublic(true);
        server.setOwnerId(ownerId);
        server.setAvatar(null);
        server.setOwner(owner);

        if (server.getMembers() == null) {
            server.setMembers(new ArrayList<>());
        }
        // 3) Save the server
        server = serverRepository.save(server);

        // 4) Automatically join the owner to the server by calling joinServer
        joinServer(server.getServerName(), ownerId);

        // 5) Return the fully created server
        return server;
    }

    // Delete a server
    @Async("securityAwareExecutor")
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
    @Async("securityAwareExecutor")
    public CompletableFuture<List<ServerDto>> getUserServers(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                // 1. Fetch servers for the given user
                List<Server> servers = serverRepository.findAllByMemberUserId(userId);

                // 2. Log if none found
                if (servers.isEmpty()) {
                    System.out.println("No servers found for user " + userId);
                }

                // 3. Convert each Server entity to a ServerDto, providing defaults if null
                return servers.stream()
                        .map(server -> new ServerDto(
                                server.getServerId(),
                                server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                                server.getDescription() != null ? server.getDescription() : "No description available",
                                server.getIsPublic() != null ? server.getIsPublic() : false
                        ))
                        .collect(Collectors.toList());

            } catch (Exception e) {
                System.out.println("Error getting servers for user " + userId + ": " + e.getMessage());
                e.printStackTrace();
                return List.of(); // Return an empty list if something goes wrong
            }
        });
    }

    @Transactional
    public List<ServerDto> getUserServersFromUserSide(int userId) {
        // 1) Load the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        // 2) Access the servers from 'user'
        System.out.println("User: "+ user.getServers() + " " + user.getUserName());
        List<Server> servers = user.getServers();  // This is the ManyToMany list

        // 3) Convert to Dtos
        return servers.stream()
                .map(this::toDto)   // or a custom mapping
                .collect(Collectors.toList());
    }

    private ServerDto toDto(Server server) {
        return new ServerDto(
                server.getServerId(),
                server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                server.getDescription() != null ? server.getDescription() : "No description",
                server.getIsPublic() != null ? server.getIsPublic() : false
        );
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<List<ServerDto>> getAllServers() {
        return CompletableFuture.supplyAsync(() -> {
            try{
                List<Server> servers = serverRepository.findAll();
                if (servers.isEmpty()) {
                    System.out.println("No servers found in the database" + servers);
                }
                // Convert Server entities to ServerResponse DTOs
                return servers.stream()
                        .map(server -> new ServerDto(
                                server.getServerId(),
                                server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                                server.getDescription() != null ? server.getDescription() : "No description available",
                                server.getIsPublic() != null ? server.getIsPublic() : false
                        ))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                System.out.println("Error getting servers: " + e.getMessage());
                e.printStackTrace();
                return List.of(); // Return an empty list if something goes wrong
            }
        });
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<ServerDto> editServer(
            int serverId,
            int ownerId,
            String newName,
            String newDescription,
            String newAvatar
    ) {
        return CompletableFuture.supplyAsync(() -> {
            // 1. Fetch server from DB
            Server server = serverRepository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Server not found"));

            // 2. Ensure the user owns the server
            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to edit this server");
            }

            // 3. Update fields
            if (newName != null) {
                server.setServerName(newName);
            }
            if (newDescription != null) {
                server.setDescription(newDescription);
            }
            /*if (newAvatar != null) {
                server.setAvatar(newAvatar);
            }*/

            // 4. Save changes
            Server updatedServer = serverRepository.save(server);

            // 5. Convert updated entity to a ServerDto
            return new ServerDto(
                    updatedServer.getServerId(),
                    updatedServer.getServerName(),
                    updatedServer.getDescription(),
                    updatedServer.getIsPublic() != null ? updatedServer.getIsPublic() : false
            );
        });
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<ServerDto> getServerById(int serverId) {
        return CompletableFuture.supplyAsync(() -> {
            // Fetch the server from the database
            Server server = serverRepository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));

            // Convert to a ServerDto (handle null fields safely)
            return new ServerDto(
                    server.getServerId(),
                    server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                    server.getDescription() != null ? server.getDescription() : "No description available",
                    server.getIsPublic() != null ? server.getIsPublic() : false
            );
        });
    }

    @Transactional
    public void joinServer(String serverName, int userId) {
        // 1) find the server by name
        Server server = serverRepository.findByServerName(serverName)
                .orElseThrow(() -> new IllegalArgumentException("No server found with name: " + serverName));

        // 2) find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with id: " + userId));

        // 3) Add user to server's members if not already
        if (!server.getMembers().contains(user)) {
            server.getMembers().add(user);
        }

        // 4) Also add the server to user’s servers if not already
        if (!user.getServers().contains(server)) {
            user.getServers().add(server);
        }

        // 5) Save the "owning" side or both to ensure the link is persisted
        // Typically, the side with @JoinTable is the "owning" side - that’s the user in this example
        // So saving user might be enough. But to be safe, you can save both:

        userRepository.save(user);   // Persist the new link
        serverRepository.save(server);

    }
}