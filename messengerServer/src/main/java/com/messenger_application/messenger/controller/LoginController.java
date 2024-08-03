package com.messenger_application.messenger.controller;

import com.messenger_application.messenger.common.APIResponse;
import com.messenger_application.messenger.dto.LoginDTO;
import com.messenger_application.messenger.dto.SignupDTO;
import com.messenger_application.messenger.service.framework.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> userRegistration(@RequestBody SignupDTO signupDTO)
    {
        APIResponse apiResponse = loginService.userRegistration(signupDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> userLogin(@RequestBody LoginDTO loginDTO)
    {
        APIResponse apiResponse = loginService.userLogin(loginDTO);

        return ResponseEntity
                .status(apiResponse.getStatus())
                .body(apiResponse);
    }

}
