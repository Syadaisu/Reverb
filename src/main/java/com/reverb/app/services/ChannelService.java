package com.reverb.app.services;

import com.reverb.app.dto.requests.AddChannelRequest;
import com.reverb.app.dto.requests.EditChannelRequest;
import com.reverb.app.dto.responses.ChannelDto;
import com.reverb.app.models.Channel;
import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.ChannelRepository;
import com.reverb.app.repositories.ServerRepository;
import com.reverb.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChannelService(ChannelRepository channelRepository,
                          ServerRepository serverRepository,
                          UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.serverRepository = serverRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new channel for a server, ensuring only the server owner can do so.
     */
    @Async("securityAwareExecutor")
    public CompletableFuture<ChannelDto> createChannel(int ownerId, AddChannelRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // 1. Fetch the server
            Server server = serverRepository.findById(request.getServerId())
                    .orElseThrow(() -> new RuntimeException("Server not found with ID: " + request.getServerId()));

            // 2. Check ownership
            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to create channels in this server.");
            }

            // 3. Create Channel entity
            Channel channel = new Channel();
            channel.setChannelName(request.getChannelName());
            channel.setRoleAccess(request.getRoleAccess());
            channel.setDescription(request.getDescription());
            channel.setServer(server);

            // 4. Save
            channel = channelRepository.save(channel);

            // 5. Return as DTO
            return new ChannelDto(
                    channel.getChannelId(),
                    channel.getChannelName(),
                    server.getServerId(),
                    channel.getRoleAccess(),
                    channel.getDescription()
            );
        });
    }

    /**
     * Get channels by serverId (anyone might be allowed to see them, or you can add an isPublic check).
     */
    @Async("securityAwareExecutor")
    public CompletableFuture<List<ChannelDto>> getChannelsByServer(int serverId) {
        return CompletableFuture.supplyAsync(() -> {
            // 1. Find all channels for the server
            List<Channel> channels = channelRepository.findByServerServerId(serverId);

            // 2. Map to DTO
            return channels.stream()
                    .map(ch -> new ChannelDto(
                            ch.getChannelId(),
                            ch.getChannelName(),
                            ch.getServer().getServerId(),
                            ch.getRoleAccess(),
                            ch.getDescription()
                    ))
                    .collect(Collectors.toList());
        });
    }

    /**
     * Get a single channel by its ID (optional: also check if the user has access).
     */
    @Async("securityAwareExecutor")
    public CompletableFuture<ChannelDto> getChannelById(int channelId) {
        return CompletableFuture.supplyAsync(() -> {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            return new ChannelDto(
                    channel.getChannelId(),
                    channel.getChannelName(),
                    channel.getServer().getServerId(),
                    channel.getRoleAccess(),
                    channel.getDescription()
            );
        });
    }

    /**
     * Edit a channel’s name, roleAccess, and description (only the server owner can edit).
     */
    @Async("securityAwareExecutor")
    public CompletableFuture<ChannelDto> editChannel(int ownerId, int channelId, EditChannelRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            // Check if the user is the owner of the server
            Server server = channel.getServer();
            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to edit this channel.");
            }

            // Update fields if provided
            if (request.getChannelName() != null) {
                channel.setChannelName(request.getChannelName());
            }
            if (request.getRoleAccess() != null) {
                channel.setRoleAccess(request.getRoleAccess());
            }
            if (request.getDescription() != null) {
                channel.setDescription(request.getDescription());
            }

            channel = channelRepository.save(channel);

            return new ChannelDto(
                    channel.getChannelId(),
                    channel.getChannelName(),
                    channel.getServer().getServerId(),
                    channel.getRoleAccess(),
                    channel.getDescription()
            );
        });
    }

    /**
     * Delete a channel (only the server owner can delete).
     */
    @Async("securityAwareExecutor")
    public CompletableFuture<Void> deleteChannel(int ownerId, int channelId) {
        return CompletableFuture.runAsync(() -> {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            // Check ownership
            if (channel.getServer().getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to delete this channel.");
            }

            channelRepository.deleteById(channelId);
        });
    }
}
