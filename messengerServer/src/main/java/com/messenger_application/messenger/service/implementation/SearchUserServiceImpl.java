package com.messenger_application.messenger.service.implementation;

import com.messenger_application.messenger.dto.SearchResultDTO;
import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.entity.User;
import com.messenger_application.messenger.repository.FriendRequestRepository;
import com.messenger_application.messenger.repository.UserRepository;
import com.messenger_application.messenger.service.framework.SearchUserService;
import com.messenger_application.messenger.status.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchUserServiceImpl implements SearchUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Override
    public List<SearchResultDTO> searchUser(String userName, Long senderId) {
        List<User> userList = userRepository.findByUserNameContainingIgnoreCase(userName);
        User sender = userRepository.findById(senderId).get();

        return userList.stream()
                .filter(user -> !user.getId().equals(senderId) )
                .map((user -> {
                    SearchResultDTO searchResultDto = new SearchResultDTO();

                    searchResultDto.setId(user.getId());
                    searchResultDto.setUserName(user.getUserName());

                    FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, user);
                    FriendRequest reverseFriendRequest = friendRequestRepository.findBySenderAndReceiver(user, sender);

                    if ((friendRequest != null && friendRequest.getStatus() == RequestStatus.ACCEPTED) ||
                            (reverseFriendRequest != null && reverseFriendRequest.getStatus() == RequestStatus.ACCEPTED)) {
                        searchResultDto.setRequestStatus(RequestStatus.ACCEPTED);
                    } else if (friendRequest != null) {
                        searchResultDto.setRequestStatus(friendRequest.getStatus());
                    } else if (reverseFriendRequest != null) {
                        searchResultDto.setRequestStatus(reverseFriendRequest.getStatus());
                    } else {
                        searchResultDto.setRequestStatus(RequestStatus.NONE);
                    }

                    return searchResultDto;

                }))
                .collect(Collectors.toList());
    }
}
