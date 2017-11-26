package com.gromoks.movieland.service.impl.security;

import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.service.security.UserTokenService;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.impl.util.JsonUserRequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserTokenService userTokenService;

    public AuthenticationServiceImpl() {}

    public AuthenticationServiceImpl(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    public UserToken getAuthentication(String loginRequest) throws AuthenticationException {
        log.info("Start to get authentication");
        LoginRequest convertedLoginRequest = JsonUserRequestConverter.convertJsonToUserRequest(loginRequest);
        if (convertedLoginRequest.getEmail().isEmpty() || convertedLoginRequest.getPassword().isEmpty()) {
            log.warn("One ot both provided request parameters are empty");
            throw new IllegalArgumentException("One ot both provided request parameters are empty");
        }
        UserToken userToken = userTokenService.getUserToken(convertedLoginRequest);
        log.info("Authentication has been passed for user {} with role {}",userToken.getUser().getNickname(),userToken.getUser().getRole());
        return userToken;
    }

    @Override
    public UserToken getAuthenticationByUuid(String uuid) throws AuthenticationException {
        log.info("Start to get authentication for uuid = {}",uuid);
        if (!uuid.isEmpty()) {
            UserToken userToken = userTokenService.getUserTokenByUuid(uuid);
            log.info("Authentication has been passed for user {} with role {}",userToken.getUser().getNickname(),userToken.getUser().getRole());
            return userToken;
        } else {
            log.warn("Uuid is empty");
            throw new IllegalArgumentException("Uuid is empty");
        }
    }

    @Override
    public void logout(String uuid) {
        userTokenService.removeUserToken(uuid);
    }
}
