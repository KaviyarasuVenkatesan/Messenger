package com.messenger_application.messenger.repository;

import com.messenger_application.messenger.entity.Message;
import com.messenger_application.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);
    List<Message> findByReceiverAndSender(User receiver, User sender);
}

