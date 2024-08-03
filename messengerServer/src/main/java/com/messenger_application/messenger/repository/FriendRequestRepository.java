package com.messenger_application.messenger.repository;

import com.messenger_application.messenger.entity.FriendRequest;
import com.messenger_application.messenger.entity.User;
import com.messenger_application.messenger.status.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    FriendRequest findBySenderAndReceiver (User senderId, User receiverId);

    List<FriendRequest> findByReceiver (User receiver);

    @Query("SELECT fr.sender FROM FriendRequest fr WHERE fr.receiver = :receiver AND fr.status = :requestStatus")
    List<User> findSenderByReceiverAndStatus(@Param("receiver") User receiver,@Param("requestStatus") RequestStatus requestStatus);

    @Query("SELECT fr.receiver FROM FriendRequest fr WHERE fr.sender = :sender AND fr.status = :requestStatus")
    List<User> findReceiverBySenderAndStatus(@Param("sender") User sender,@Param("requestStatus") RequestStatus requestStatus);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.receiver.id = :userId AND fr.status = :requestStatus")
    List<FriendRequest> findPendingRequests(@Param("userId") Long userId, @Param("requestStatus") RequestStatus requestStatus);

}
