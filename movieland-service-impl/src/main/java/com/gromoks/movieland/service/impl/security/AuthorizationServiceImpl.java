package com.gromoks.movieland.service.impl.security;

import com.gromoks.movieland.service.security.AuthorizationService;
import com.gromoks.movieland.service.security.UserTokenService;
import com.gromoks.movieland.service.entity.UserRole;
import com.gromoks.movieland.service.entity.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserTokenService userTokenService;

    @Override
    public void getAuthorizationAddReview(UserToken userToken) {
        UserRole userRole = UserRole.getByName(userToken.getUser().getRole());
        if (userRole == UserRole.USER) {
            log.info("Authorization is passed");
        } else {
            log.info("Authorization has not been passed. Ineligible user role: {} for user: {}",userToken.getUser().getRole(),userToken.getUser().getNickname());
            throw new SecurityException("Authorization has not been passed. Ineligible user role: " + userToken.getUser().getRole() + " for user: " + userToken.getUser().getNickname());
        }
    }
}
