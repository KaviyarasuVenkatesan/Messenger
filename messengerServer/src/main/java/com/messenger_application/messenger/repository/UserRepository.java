package com.messenger_application.messenger.repository;

import com.messenger_application.messenger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUserNameAndPassword(String userName, String password);

    User findOneByUserName(String userName);

    List<User> findByUserNameContainingIgnoreCase (String userName);
}



