package com.messenger_application.messenger.service.implementation;

import com.messenger_application.messenger.common.APIResponse;
import com.messenger_application.messenger.dto.LoginDTO;
import com.messenger_application.messenger.dto.SignupDTO;
import com.messenger_application.messenger.entity.User;
import com.messenger_application.messenger.repository.UserRepository;
import com.messenger_application.messenger.service.framework.LoginService;
import com.messenger_application.messenger.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtToken;

    @Override
    public APIResponse userRegistration(SignupDTO signupDTO) {

        HashMap<String, Object> extraClaims = new HashMap<>();
        APIResponse apiResponse = new APIResponse();
        User user = new User();

        String userName = signupDTO.getUserName();
        User checkUserExist = userRepository.findOneByUserName(userName);

        if(checkUserExist == null) {
            user.setFirstName(signupDTO.getFirstName());
            user.setLastName(signupDTO.getLastName());
            user.setUserName(signupDTO.getUserName());
            user.setEmail(signupDTO.getEmail());
            user.setPassword(signupDTO.getPassword());
            user.setPhoneNumber(signupDTO.getPhoneNumber());
            user.setGender(signupDTO.getGender());

            userRepository.save(user);

            String token = jwtToken.generateToken(extraClaims, user);

            apiResponse.setBody(token);

            return apiResponse;
        }
        else {
            apiResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            apiResponse.setBody("user name already exists");
            return apiResponse;
        }
    }

    public APIResponse userLogin(LoginDTO loginDTO) {

        APIResponse apiResponse = new APIResponse();
        HashMap<String, Object> extraClaims = new HashMap<>();

        User user = userRepository.findOneByUserNameAndPassword(loginDTO.getUserName(), loginDTO.getPassword());

        if (user == null){
            apiResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            apiResponse.setBody("user not found");
            return apiResponse;
        }

        Long userId = user.getId();
        extraClaims.put("id", userId);
        String token = jwtToken.generateToken(extraClaims, user);
        apiResponse.setBody(token);

        return apiResponse;
    }
}
