package com.messenger_application.messenger.service.framework;

import com.messenger_application.messenger.dto.FriendListDTO;
import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.entity.User;
import org.apache.commons.lang3.tuple.Pair;


public interface FriendRequestService {

    Pair<FriendRequest, User> sendRequest (Long senderId, Long receiverId);

    Pair<User, User> acceptRequest (Long senderId, Long receiverId);

    Pair<User, User> cancelRequest (Long senderId, Long receiverId);

    void rejectRequest (Long senderId, Long receiverId);

    FriendListDTO getFriendsList (Long userId);
}
