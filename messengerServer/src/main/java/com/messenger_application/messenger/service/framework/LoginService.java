package com.messenger_application.messenger.service.framework;

import com.messenger_application.messenger.common.APIResponse;
import com.messenger_application.messenger.dto.LoginDTO;
import com.messenger_application.messenger.dto.SignupDTO;


public interface LoginService {

    APIResponse userRegistration (SignupDTO signupDTO);

    APIResponse userLogin(LoginDTO loginDTO);

}
