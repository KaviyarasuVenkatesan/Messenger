package com.messenger_application.messenger.controller;

import com.messenger_application.messenger.entity.Message;
import com.messenger_application.messenger.service.framework.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public List<Message> getMessages(@RequestParam Long userId, @RequestParam Long friendId) {
        return messageService.getMessagesBetweenUsers(userId, friendId);
    }

    @MessageMapping("/private-message")
    public Message sendMessage(@Payload Message message) {
        System.out.println("checking"+message);
        Message savedMessage = messageService.saveMessage(
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent()
        );
        try {
            messagingTemplate.convertAndSendToUser(
                    message.getReceiver().getUserName(), // Send to the user's unique ID
                    "/private",
                    savedMessage
            );
        }
        catch (Exception ex)
        {
            throw  ex;
        }
        return savedMessage;
    }

}
