package com.gromoks.movieland.service.security;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;

import javax.naming.AuthenticationException;

public interface UserTokenService {
    UserToken getUserToken(LoginRequest loginRequest) throws AuthenticationException;
    void removeUserToken(String uuid);
    UserToken getUserTokenByUuid(String uuid) throws AuthenticationException;
}
