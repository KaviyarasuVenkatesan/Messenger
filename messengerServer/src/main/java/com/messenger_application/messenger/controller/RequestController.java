package com.messenger_application.messenger.controller;

import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.entity.User;
import com.messenger_application.messenger.service.framework.FriendRequestService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend-request")
public class RequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<Void> sendRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Pair<FriendRequest, User> response = friendRequestService.sendRequest(senderId, receiverId);
        FriendRequest friendRequest = response.getLeft();
        User receiver = response.getRight();

        simpMessagingTemplate.convertAndSendToUser(receiver.getUserName(), "/getNotification", friendRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Pair<User, User> response = friendRequestService.acceptRequest(senderId, receiverId);
        User sender = response.getLeft();
        User receiver = response.getRight();

        simpMessagingTemplate.convertAndSendToUser(sender.getUserName(), "/friends", receiver);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        friendRequestService.rejectRequest(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelRequest(@RequestParam Long senderId, @RequestParam Long receiverId) {
        Pair<User, User> response = friendRequestService.cancelRequest(senderId, receiverId);
        User sender = response.getLeft();
        User receiver = response.getRight();

        simpMessagingTemplate.convertAndSendToUser(receiver.getUserName(), "/updateNotification", sender
        );
        return ResponseEntity.ok().build();
    }

}
