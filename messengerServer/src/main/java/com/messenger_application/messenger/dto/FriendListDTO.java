package com.messenger_application.messenger.dto;

import com.messenger_application.messenger.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FriendListDTO {

    private List<User> friends;

}
