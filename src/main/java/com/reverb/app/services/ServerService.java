package com.reverb.app.services;

import com.reverb.app.dto.responses.ServerDto;
import com.reverb.app.models.Attachment;
import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.AttachmentRepository;
import com.reverb.app.repositories.ChannelRepository;
import com.reverb.app.repositories.ServerRepository;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository, UserRepository userRepository, ChannelRepository channelRepository, AttachmentRepository attachmentRepository) {
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Transactional
    @Async("securityAwareExecutor")
    public CompletableFuture<Server> addServer(String serverName, String description, int ownerId) {
        return CompletableFuture.supplyAsync(() -> {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new UsernameNotFoundException("Owner not found"));

            Server server = new Server();
            server.setServerName(serverName);
            server.setDescription(description);
            server.setOwnerId(ownerId);
            server.setServerIcon(null);
            server.setOwner(owner); // Link the owner


            if (server.getMembers() == null) {
                server.setMembers(new ArrayList<>());
            }

            if (server.getAuthorizedUsers() == null) {
                server.setAuthorizedUsers(new ArrayList<>());
            }
            server = serverRepository.save(server);
            joinServer(serverName, ownerId);
            grantAuthority(serverName, ownerId);

            return server;
        });
    }

    @Transactional
    public Server addServerSync(String name, String description, int ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UsernameNotFoundException("Owner not found"));

        Server server = new Server();
        server.setServerName(name);
        server.setDescription(description);
        server.setOwnerId(ownerId);
        server.setServerIcon(null);
        server.setOwner(owner);

        if (server.getMembers() == null) {
            server.setMembers(new ArrayList<>());
        }

        if (server.getAuthorizedUsers() == null) {
            server.setAuthorizedUsers(new ArrayList<>());
        }
        server = serverRepository.save(server);

        joinServer(server.getServerName(), ownerId);
        grantAuthority(server.getServerName(), ownerId);

        //System.out.println("Server created: " + server.getServerName() + " by " + owner.getUserName() + " authority check " + server.getAuthorizedUsers());
        return server;
    }

    // Delete a server
    @Async("securityAwareExecutor")
    public CompletableFuture<Void> deleteServer(int serverId, int ownerId) {
        return CompletableFuture.runAsync(() -> {
            Server server = serverRepository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Server not found"));

            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to delete this server");
            }
            channelRepository.deleteAllByServerId(serverId);
            serverRepository.deleteById(serverId);
        });
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<List<ServerDto>> getUserServers(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                List<Server> servers = serverRepository.findAllByMemberUserId(userId);

                /*if (servers.isEmpty()) {
                    System.out.println("No servers found for user " + userId);
                }*/

                return servers.stream()
                        .map(server -> new ServerDto(
                                server.getServerId(),
                                server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                                server.getDescription() != null ? server.getDescription() : "No description available",
                                server.getOwnerId(),
                                server.getServerIcon() != null ? server.getServerIcon().getAttachmentUuid() : null
                        ))
                        .collect(Collectors.toList());

            } catch (Exception e) {
                //System.out.println("Error getting servers for user " + userId + ": " + e.getMessage());
                e.printStackTrace();
                return List.of(); // Return an empty list if something goes wrong
            }
        });
    }

    @Transactional
    public List<ServerDto> getUserServersFromUserSide(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + userId));

        //System.out.println("User: "+ user.getServers() + " " + user.getUserName());
        List<Server> servers = user.getServers();

        return servers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ServerDto toDto(Server server) {
        return new ServerDto(
                server.getServerId(),
                server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                server.getDescription() != null ? server.getDescription() : "No description",
                server.getOwnerId(),
                server.getServerIcon() != null ? server.getServerIcon().getAttachmentUuid() : null

        );
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<List<ServerDto>> getAllServers() {
        return CompletableFuture.supplyAsync(() -> {
            try{
                List<Server> servers = serverRepository.findAll();
                if (servers.isEmpty()) {
                    //System.out.println("No servers found in the database" + servers);
                }
                return servers.stream()
                        .map(server -> new ServerDto(
                                server.getServerId(),
                                server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                                server.getDescription() != null ? server.getDescription() : "No description available",
                                server.getOwnerId(),
                                server.getServerIcon() != null ? server.getServerIcon().getAttachmentUuid() : null
                        ))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                System.out.println("Error getting servers: " + e.getMessage());
                e.printStackTrace();
                return List.of();
            }
        });
    }

    @Transactional
    @Async("securityAwareExecutor")
    public CompletableFuture<ServerDto> editServer(
            int serverId,
            int ownerId,
            String newName,
            String newDescription
    ) {
        return CompletableFuture.supplyAsync(() -> {


            Server server = serverRepository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Server not found"));


            /*if (server.getOwnerId() != ownerId ) {
                throw new RuntimeException("You do not have permission to edit this server");
            }*/



            if (newName != null) {
                server.setServerName(newName);
            }
            if (newDescription != null) {
                server.setDescription(newDescription);
            }

            Server updatedServer = serverRepository.save(server);

            return new ServerDto(
                    updatedServer.getServerId(),
                    updatedServer.getServerName(),
                    updatedServer.getDescription(),
                    updatedServer.getOwnerId(),
                    updatedServer.getServerIcon() != null ? updatedServer.getServerIcon().getAttachmentUuid() : null
            );
        });
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<ServerDto> getServerById(int serverId) {
        return CompletableFuture.supplyAsync(() -> {
            Server server = serverRepository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));

            return new ServerDto(
                    server.getServerId(),
                    server.getServerName() != null ? server.getServerName() : "Unnamed Server",
                    server.getDescription() != null ? server.getDescription() : "No description available",
                    server.getOwnerId(),
                    server.getServerIcon() != null ? server.getServerIcon().getAttachmentUuid() : null
            );
        });
    }

    @Transactional
    public void joinServer(String serverName, int userId) {
        Server server = serverRepository.findByServerName(serverName)
                .orElseThrow(() -> new IllegalArgumentException("No server found with name: " + serverName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with id: " + userId));

        if (!server.getMembers().contains(user)) {
            server.getMembers().add(user);
        }

        if (!user.getServers().contains(server)) {
            user.getServers().add(server);
        }

        userRepository.save(user);   // Persist the new link
        serverRepository.save(server);
    }

    @Transactional
    public void leaveServer(String serverName,int userId) {
        Server server = serverRepository.findByServerName(serverName)
                .orElseThrow(() -> new IllegalArgumentException("No server found with name: " + serverName));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with id: " + userId));

        if (server.getMembers().contains(user)) {
            server.getMembers().remove(user);
        }
        if (user.getServers().contains(server)) {
            user.getServers().remove(server);
        }
        userRepository.save(user);
        serverRepository.save(server);
    }

    @Transactional
    public void grantAuthority(String serverName, int userId){
        Server server = serverRepository.findByServerName(serverName)
                .orElseThrow(() -> new IllegalArgumentException("No server found with name: " + serverName));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with id: " + userId));

        if (!server.getAuthorizedUsers().contains(user)) {
            server.getAuthorizedUsers().add(user);
        }

        if (!user.getAuthorizedServers().contains(server)) {
            user.getAuthorizedServers().add(server);
        }

        //System.out.println("Server with admin users: " + server.getAuthorizedUsers());
        userRepository.save(user);   // Persist the new link
        serverRepository.save(server);
    }

    @Transactional
    public void grantAuthorityByEmail(int serverId, String email){
        Server server = serverRepository.findByServerId(serverId)
                .orElseThrow(() -> new IllegalArgumentException("No server found with name: " + serverId));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        //System.out.println("User email found: " + user.getEmail());
        if (!server.getAuthorizedUsers().contains(user)) {
            server.getAuthorizedUsers().add(user);
        }

        if (!user.getAuthorizedServers().contains(server)) {
            user.getAuthorizedServers().add(server);
        }

        //System.out.println("Server with admin users: " + server.getAuthorizedUsers());
        userRepository.save(user);   // Persist the new link
        serverRepository.save(server);
    }

    @Transactional
    public void revokeAuthority (String serverName, String email){
        Server server = serverRepository.findByServerName(serverName)
                .orElseThrow(() -> new IllegalArgumentException("No server found with name: " + serverName));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No user found with email: " + email));

        if (server.getAuthorizedUsers().contains(user)) {
            server.getAuthorizedUsers().remove(user);
        }

        serverRepository.save(server);
    }

    @Transactional
    public void updateServerAvatar(int serverId, MultipartFile serverIcon) throws Exception {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new Exception("Authenticated user not found"));

        if (serverIcon == null || serverIcon.isEmpty()) {
            throw new Exception("No avatar file provided.");
        }

        Attachment attachment;
        if (server.getServerIcon() != null) {
            attachment = server.getServerIcon();
        } else {
            attachment = new Attachment();
        }
        attachment.setAttachmentData(serverIcon.getBytes());
        attachment.setContentType(serverIcon.getContentType());
        //System.out.println("AttachmentService.uploadAvatar: " + attachment.getAttachmentUuid() + " "
        // + attachment.getContentType() + " " + attachment.getAttachmentData().length);
        attachmentRepository.save(attachment); // ensure stored

        server.setServerIcon(attachment);
        //System.out.println("ServerService.updateServerAvatar: " + server.getServerIcon().getAttachmentUuid() + " " + server.getServerIcon().getContentType() + " " + server.getServerIcon().getAttachmentData().length);
        serverRepository.save(server);
    }

    @Transactional
    public List<Integer> getServerAdminIds(int serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));
        List<User> admins = server.getAuthorizedUsers();
        List<Integer> adminIds = admins.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
        //System.out.println("Admins for server " + serverId + ": " + adminIds);
        return adminIds;
    }
}