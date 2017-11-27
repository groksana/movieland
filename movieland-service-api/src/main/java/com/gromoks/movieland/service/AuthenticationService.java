package com.gromoks.movieland.service;

import com.gromoks.movieland.service.entity.UserToken;

public interface AuthenticationService {
    UserToken getAuthentication(String loginRequest);
    void logout(String uuid);
}
