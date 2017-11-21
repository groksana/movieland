package com.gromoks.movieland.service;

import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;

public interface UserCache {
    UserToken getUserToken(LoginRequest loginRequest);
    void removeUserToken(String uuid);
    void invalidate();
}
