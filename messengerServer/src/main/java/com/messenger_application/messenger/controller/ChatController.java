package com.messenger_application.messenger.controller;

import com.messenger_application.messenger.dto.FriendListDTO;
import com.messenger_application.messenger.service.framework.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/friends")
    public ResponseEntity<FriendListDTO> fetchFriendsList (@RequestParam Long userId){
        FriendListDTO friendList = friendRequestService.getFriendsList(userId);

        return ResponseEntity.ok(friendList);

    }

}
