package com.reverb.app.services;

import com.reverb.app.dto.requests.AddChannelRequest;
import com.reverb.app.dto.requests.EditChannelRequest;
import com.reverb.app.dto.responses.ChannelDto;
import com.reverb.app.models.Channel;
import com.reverb.app.models.Server;
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


    @Async("securityAwareExecutor")
    public CompletableFuture<ChannelDto> createChannel(int ownerId, AddChannelRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Server server = serverRepository.findById(request.getServerId())
                    .orElseThrow(() -> new RuntimeException("Server not found with ID: " + request.getServerId()));

            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to create channels in this server.");
            }

            Channel channel = new Channel();
            channel.setChannelName(request.getChannelName());
            channel.setDescription(request.getDescription());
            channel.setServer(server);

            channel = channelRepository.save(channel);

            return new ChannelDto(
                    channel.getChannelId(),
                    channel.getChannelName(),
                    server.getServerId(),
                    channel.getDescription()
            );
        });
    }

    public Channel createChannelSync(String name, String description, int serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new UsernameNotFoundException("Server not found"));

        Channel channel = new Channel();
        channel.setChannelName(name);
        channel.setDescription(description);
        channel.setServer(server);
        return channelRepository.save(channel);
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<List<ChannelDto>> getChannelsByServer(int serverId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Channel> channels = channelRepository.findByServerServerId(serverId);

            return channels.stream()
                    .map(ch -> new ChannelDto(
                            ch.getChannelId(),
                            ch.getChannelName(),
                            ch.getServer().getServerId(),
                            ch.getDescription()
                    ))
                    .collect(Collectors.toList());
        });
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<ChannelDto> getChannelById(int channelId) {
        return CompletableFuture.supplyAsync(() -> {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            return new ChannelDto(
                    channel.getChannelId(),
                    channel.getChannelName(),
                    channel.getServer().getServerId(),
                    channel.getDescription()
            );
        });
    }

    @Async("securityAwareExecutor")
    public CompletableFuture<ChannelDto> editChannel(int ownerId, int channelId, EditChannelRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            Server server = channel.getServer();
            if (server.getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to edit this channel.");
            }

            if (request.getChannelName() != null) {
                channel.setChannelName(request.getChannelName());
            }

            if (request.getDescription() != null) {
                channel.setDescription(request.getDescription());
            }

            channel = channelRepository.save(channel);

            return new ChannelDto(
                    channel.getChannelId(),
                    channel.getChannelName(),
                    channel.getServer().getServerId(),
                    channel.getDescription()
            );
        });
    }


    @Async("securityAwareExecutor")
    public CompletableFuture<Void> deleteChannel(int ownerId, int channelId) {
        return CompletableFuture.runAsync(() -> {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

            if (channel.getServer().getOwnerId() != ownerId) {
                throw new RuntimeException("You do not have permission to delete this channel.");
            }

            channelRepository.deleteById(channelId);
        });
    }
}
