package com.gromoks.movieland.service.impl.security;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.security.AuthorizationService;
import com.gromoks.movieland.service.security.UserTokenService;
import com.gromoks.movieland.service.entity.UserRole;
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
    public void authorizeToAddReview(User user) {
        UserRole userRole = UserRole.getByName(user.getRole());
        if (userRole == UserRole.USER) {
            log.info("Authorization is passed");
        } else {
            log.info("Authorization has not been passed. Ineligible user role: {} for user: {}",user.getRole(),user.getNickname());
            throw new SecurityException("Authorization has not been passed. Ineligible user role: " + user.getRole() + " for user: " + user.getNickname());
        }
    }
}
