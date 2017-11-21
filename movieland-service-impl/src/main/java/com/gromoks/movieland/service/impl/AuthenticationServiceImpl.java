package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.AuthenticationService;
import com.gromoks.movieland.service.UserCache;
import com.gromoks.movieland.service.UserService;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.impl.util.JsonUserRequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Override
    public UserToken getAuthentication(String loginRequest) {
        LoginRequest convertedLoginRequest = JsonUserRequestConverter.convertJsonToUserRequest(loginRequest);
        UserToken userToken = userService.getUserToken(convertedLoginRequest);
        return userToken;
    }

    @Override
    public void logout(String uuid) {
        userService.removeUserToken(uuid);
    }
}
