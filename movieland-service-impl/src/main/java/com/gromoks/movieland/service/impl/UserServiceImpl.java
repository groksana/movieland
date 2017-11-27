package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.service.UserCache;
import com.gromoks.movieland.service.UserService;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserCache userCache;

    @Override
    public UserToken getUserToken(LoginRequest loginRequest) {
        return userCache.getUserToken(loginRequest);
    }

    @Override
    public void removeUserToken(String uuid) {
        userCache.removeUserToken(uuid);
    }
}
