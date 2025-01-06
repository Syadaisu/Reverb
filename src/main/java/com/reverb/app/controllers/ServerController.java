package com.reverb.app.controllers;

import com.reverb.app.dto.requests.AddFileRequest;
import com.reverb.app.dto.requests.CreateServerRequest;
import com.reverb.app.dto.requests.EditServerRequest;
import com.reverb.app.dto.responses.GenericResponse;
import com.reverb.app.dto.responses.ServerDto;
import com.reverb.app.dto.responses.UserDto;
import com.reverb.app.models.Server;
import com.reverb.app.models.User;
import com.reverb.app.repositories.ServerRepository;
import com.reverb.app.services.ChatHubService;
import com.reverb.app.services.FileService;
import com.reverb.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/server")
public class ServerController {

    private final ServerRepository serverRepository;
    private final UserService userService;
    private final FileService fileService;
    private final ChatHubService hubService;

    @Autowired
    public ServerController(ServerRepository serverRepository, UserService userService, FileService fileService, ChatHubService hubService) {
        this.serverRepository = serverRepository;
        this.userService = userService;
        this.fileService = fileService;
        this.hubService = hubService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateServerRequest dto, @AuthenticationPrincipal User user) {
        if (serverRepository.existsByServerName(dto.getServerName())) {
            return ResponseEntity.status(409).build();
        }

        int userId = userService.getUserId(user);

        Server server = new Server(dto.getServerName(), dto.getDescription(), dto.isPublic(), userId);
        serverRepository.save(server);

        serverRepository.addUserToServer(server.getServerId(), userId);

        ServerDto response = new ServerDto(server.getServerId(), server.getServerName(), server.getDescription(), server.getAvatar(), server.getOwnerId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addPicture")
    @Transactional
    public ResponseEntity<?> addPicture(@RequestParam int serverId, @RequestParam AddFileRequest formFile, @AuthenticationPrincipal User user) throws IOException {
        int userId = userService.getUserId(user);
        Server server = serverRepository.findByIdAndOwnerId(serverId, userId);
        if (server == null) {
            return ResponseEntity.status(401).build();
        }

        byte[] blob = formFile.getFile();

        try {
            if (server.getAvatar() != null) {
                fileService.delete(server.getAvatar());
            }

            UUID uuid = fileService.writeBytes(formFile.getFile());
            serverRepository.updatePictureId(serverId, uuid);
            serverRepository.save(server);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

        ServerResponse response = new ServerResponse(server.getId(), server.getName(), server.getDescription(), server.getPictureId(), server.getOwnerId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getServers")
    public ResponseEntity<?> getServers(@AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);

        List<Server> servers = serverRepository.findAllByUserId(userId);

        List<ServerDto> response = servers.stream()
                .map(server -> new ServerDto(server.getServerId(), server.getServerName(), server.getDescription(), server.getAvatar(), server.getOwnerId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getServer")
    public ResponseEntity<?> getServer(@RequestParam int id, @AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);
        Server server = serverRepository.findByServerId(id);
        if (server == null) {
            return ResponseEntity.status(404).build();
        }
        if (!serverRepository.existsByUserIdAndServerId(userId, id)) {
            return ResponseEntity.status(401).build();
        }

        ServerDto response = new ServerDto(server.getServerId(), server.getServerName(), server.getDescription(), server.getAvatar(), server.getOwnerId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/joinServer")
    public ResponseEntity<?> joinServer(@RequestParam String name, @AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);
        Server server = serverRepository.findByServerName(name);
        if (server == null || !server.isPublic()) {
            return ResponseEntity.status(404).build();
        }

        try {
            serverRepository.addUserToServer(server.getServerId(), userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericResponse("Error", "User is already on the server"));
        }

        ServerDto response = new ServerDto(server.getServerId(), server.getServerName(), server.getDescription(), server.getAvatar(), server.getOwnerId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/leaveServer")
    public ResponseEntity<?> leaveServer(@RequestParam int serverId, @AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);
        if (!serverRepository.existsByUserIdAndServerId(userId, serverId)) {
            return ResponseEntity.status(404).build();
        }

        serverRepository.removeUserFromServer(userId, serverId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteServer")
    @Transactional
    public ResponseEntity<?> deleteServer(@RequestParam int serverId, @AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);
        Server server = serverRepository.findByServerIdAndOwnerId(serverId, userId);
        if (server == null) {
            return ResponseEntity.status(404).build();
        }

        try {
            serverRepository.deleteMessagesByServerId(serverId);
            serverRepository.deleteChannelsByServerId(serverId);
            serverRepository.deleteUserServersByServerId(serverId);
            serverRepository.deleteById(serverId);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/kickUser")
    public ResponseEntity<?> kickUser(@RequestParam int serverId, @RequestParam int userId, @AuthenticationPrincipal User user) {
        int ownerId = userService.getUserId(user);
        if (!serverRepository.existsByOwnerIdAndServerId(ownerId, serverId)) {
            return ResponseEntity.status(401).build();
        }

        if (!serverRepository.existsByUserIdAndServerId(userId, serverId)) {
            return ResponseEntity.status(404).build();
        }

        serverRepository.removeUserFromServer(userId, serverId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestParam int id, @AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);

        if (!serverRepository.existsByUserIdAndServerId(userId, id)) {
            return ResponseEntity.status(401).build();
        }

        List<UserDto> users = serverRepository.findUsersByServerId(id).stream()
                .map(usernew -> new UserDto(user.getUserId(), user.getUserName(), user.getEmail(), user.getCreationDate(), user.getAvatar()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> edit(@RequestParam int serverId, @RequestBody EditServerRequest dto, @AuthenticationPrincipal User user) {
        int userId = userService.getUserId(user);
        if (!serverRepository.existsByOwnerIdAndServerId(userId, serverId)) {
            return ResponseEntity.status(401).build();
        }

        serverRepository.updateServer(serverId, dto.getName(), dto.getDescription());
        hubService.editServer(serverId, dto.getName(), dto.getDescription());
        return ResponseEntity.ok().build();
    }
}