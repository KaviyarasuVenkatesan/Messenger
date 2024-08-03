package com.messenger_application.messenger.service.implementation;

import com.messenger_application.messenger.dto.FriendListDTO;
import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.entity.User;
import com.messenger_application.messenger.repository.FriendRequestRepository;
import com.messenger_application.messenger.repository.UserRepository;
import com.messenger_application.messenger.service.framework.FriendRequestService;
import com.messenger_application.messenger.status.RequestStatus;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User findingSender (Long senderId){

        return userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    private User findingReceiver (Long receiverId){

        return userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    @Override
    public Pair<FriendRequest, User> sendRequest(Long senderId, Long receiverId) {

        User sender = findingSender(senderId);
        User receiver = findingReceiver(receiverId);

        FriendRequest existingRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver);

        FriendRequest friendRequest = new FriendRequest();

        if (existingRequest == null) {
            friendRequest.setSender(sender);
            friendRequest.setReceiver(receiver);
            friendRequest.setStatus(RequestStatus.PENDING);
        } else if (existingRequest.getStatus() == RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Request already send");
        }

        friendRequestRepository.save(friendRequest);

        return new ImmutablePair<>(friendRequestRepository.save(friendRequest), receiver);
    }

    @Override
    public Pair<User, User> acceptRequest(Long senderId, Long receiverId) {

        User sender = findingSender(senderId);
        User receiver = findingReceiver(receiverId);

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender,receiver);

        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        return new ImmutablePair<>(sender, receiver);
    }

    @Override
    public void rejectRequest(Long senderId, Long receiverId) {

        User sender = findingSender(senderId);
        User receiver = findingReceiver(receiverId);

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender,receiver);

        friendRequestRepository.delete(friendRequest);
    }

    @Override
    public Pair<User, User> cancelRequest(Long senderId, Long receiverId) {

        User sender = findingSender(senderId);
        User receiver = findingReceiver(receiverId);

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender,receiver);

        friendRequestRepository.delete(friendRequest);

        return new ImmutablePair<>(sender, receiver);
    }

    @Override
    public FriendListDTO getFriendsList(Long userId) {

        User user = findingReceiver(userId);

        List<User> sender = friendRequestRepository.findSenderByReceiverAndStatus(user, RequestStatus.ACCEPTED);
        List<User> receiver = friendRequestRepository.findReceiverBySenderAndStatus(user,RequestStatus.ACCEPTED);

        Set<User> uniqueFriends = new HashSet<>();
        uniqueFriends.addAll(sender);
        uniqueFriends.addAll(receiver);

        uniqueFriends.removeIf(friend -> friend.getId().equals(userId));

        FriendListDTO friendListDTO = new FriendListDTO();
        friendListDTO.setFriends(new ArrayList<>(uniqueFriends));

        return friendListDTO;
    }

}
