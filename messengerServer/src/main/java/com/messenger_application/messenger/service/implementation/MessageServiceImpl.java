package com.messenger_application.messenger.service.implementation;

import com.messenger_application.messenger.entity.Message;
import com.messenger_application.messenger.entity.User;
import com.messenger_application.messenger.repository.MessageRepository;
import com.messenger_application.messenger.repository.UserRepository;
import com.messenger_application.messenger.service.framework.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElse(null);
        User user2 = userRepository.findById(userId2).orElse(null);
        if (user1 == null || user2 == null) {
            return null;
        }
        List<Message> messages = messageRepository.findBySenderAndReceiver(user1, user2);
        messages.addAll(messageRepository.findByReceiverAndSender(user1, user2));
        messages.sort((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()));
        return messages;
    }

    public Message saveMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (sender == null || receiver == null) {
            return null;
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(new Date());
        return messageRepository.save(message);
    }
}

