package com.messenger_application.messenger.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

    private String senderName;
    private String receiverName;
    private String message;

}
