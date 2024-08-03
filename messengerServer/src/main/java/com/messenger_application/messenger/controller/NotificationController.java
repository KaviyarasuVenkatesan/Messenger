package com.messenger_application.messenger.controller;

import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.service.framework.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequest>> messengerNotification (@RequestParam Long userId){

        List<FriendRequest> friendRequestList = notificationService.messengerNotificationService(userId);

        return ResponseEntity.ok(friendRequestList);
    }

}
