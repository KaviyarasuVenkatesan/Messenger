package com.messenger_application.messenger.service.implementation;

import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.repository.FriendRequestRepository;
import com.messenger_application.messenger.repository.UserRepository;
import com.messenger_application.messenger.service.framework.NotificationService;
import com.messenger_application.messenger.status.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<FriendRequest> messengerNotificationService(Long userId) {

        return friendRequestRepository.findPendingRequests(userId, RequestStatus.PENDING);

    }

}
