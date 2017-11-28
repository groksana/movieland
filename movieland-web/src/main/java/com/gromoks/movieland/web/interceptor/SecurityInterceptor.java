package com.gromoks.movieland.web.interceptor;

import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.entity.UserRole;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.security.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);
    private static final String GUEST = "guest";

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserRole userRole;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(Protected.class)) {
            userRole = method.getAnnotation(Protected.class).value();
        } else {
            userRole = UserRole.GUEST;
        }

        String uuid = request.getHeader("uuid");
        if (uuid != null) {
            User user = authenticate(uuid);
            authorize(user, userRole);
        }
        return true;
    }

    private User authenticate(String uuid) throws AuthenticationException {
        UserToken userToken;
        try {
            userToken = authenticationService.getAuthenticationByUuid(uuid);
        } catch (AuthenticationException e) {
            log.error("Expired or invalid uuid");
            throw new AuthenticationException("Expired or invalid uuid");
        }
        authenticationService.setAuthenticatedUser(userToken.getUser());
        return userToken.getUser();
    }

    private void authorize(User user, UserRole requiredUserRole) {
        UserRole userRole = UserRole.getByName(user.getRole());
        if (userRole == requiredUserRole) {
            log.info("Authorization is passed");
        } else {
            log.info("Authorization has not been passed. Ineligible user role: {} for user: {}", user.getRole(), user.getNickname());
            throw new SecurityException("Authorization has not been passed. Ineligible user role: "
                    + user.getRole() + " for user: " + user.getNickname());
        }

    }
}
