package com.messenger_application.messenger.service.framework;

import com.messenger_application.messenger.dto.SearchResultDTO;

import java.util.List;

public interface SearchUserService {

    List<SearchResultDTO> searchUser (String userName, Long senderId);
}
