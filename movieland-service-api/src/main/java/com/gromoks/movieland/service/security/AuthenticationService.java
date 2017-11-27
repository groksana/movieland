package com.gromoks.movieland.service.security;

import com.gromoks.movieland.service.entity.UserToken;

import javax.naming.AuthenticationException;

public interface AuthenticationService {
    UserToken getAuthentication(String loginRequest) throws AuthenticationException;
    UserToken getAuthenticationByUuid(String uuid) throws AuthenticationException;
    void logout(String uuid);
}
