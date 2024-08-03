package com.messenger_application.messenger.controller;

import com.messenger_application.messenger.dto.SearchResultDTO;
import com.messenger_application.messenger.service.framework.SearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private SearchUserService searchUserService;

    @GetMapping("/search")
    public List<SearchResultDTO> searchUser (@RequestParam String userName , @RequestParam Long senderId)
    {
        List<SearchResultDTO> searchResult = searchUserService.searchUser(userName, senderId);

        return searchResult;
    }
}
