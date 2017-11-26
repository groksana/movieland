package com.gromoks.movieland.service.security;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;

import javax.naming.AuthenticationException;

public interface UserTokenCache {
    void removeCacheUserToken(String uuid);
    void invalidate();
    void cacheUserToken(UserToken userToken);
    UserToken getCacheUserTokenByUuid(String uuid) throws AuthenticationException;
}
