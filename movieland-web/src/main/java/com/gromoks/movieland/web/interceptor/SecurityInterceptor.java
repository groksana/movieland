package com.gromoks.movieland.web.interceptor;

import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.security.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);
    private static final String GUEST = "guest";

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uuid = request.getHeader("uuid");
        authenticate(uuid);

        return true;
    }

    private void authenticate(String uuid) throws AuthenticationException {
        if (uuid != null) {
            UserToken userToken;
            try {
                userToken = authenticationService.getAuthenticationByUuid(uuid);
            } catch (AuthenticationException e) {
                log.error("Expired or invalid uuid");
                throw new AuthenticationException("Expired or invalid uuid");
            }
            authenticationService.setAuthenticatedUser(userToken.getUser());
        }
    }
}
