package com.messenger_application.messenger.dto;

import com.messenger_application.messenger.status.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultDTO {

    private Long id;
    private String userName;
    private RequestStatus requestStatus;

}
