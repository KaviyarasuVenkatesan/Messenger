package com.messenger_application.messenger.service.framework;

import com.messenger_application.messenger.entity.FriendRequest;

import java.util.List;

public interface NotificationService {

    List<FriendRequest> messengerNotificationService (Long userId);
}
