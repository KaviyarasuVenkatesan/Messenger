package com.messenger_application.messenger.service.framework;

import com.messenger_application.messenger.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> getMessagesBetweenUsers(Long userId1, Long userId2);

    Message saveMessage(Long senderId, Long receiverId, String content);

}
