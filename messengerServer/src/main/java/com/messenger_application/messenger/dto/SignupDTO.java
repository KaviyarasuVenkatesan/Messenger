package com.messenger_application.messenger.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDTO {

    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String gender;

}
