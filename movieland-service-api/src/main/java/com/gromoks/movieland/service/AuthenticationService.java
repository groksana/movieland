package com.gromoks.movieland.service;

import com.gromoks.movieland.service.entity.UserToken;

public interface AuthenticationService {
    UserToken getAuthentication(String loginRequest);
    UserToken getAuthenticationByUuid(String uuid);
    void logout(String uuid);
}
