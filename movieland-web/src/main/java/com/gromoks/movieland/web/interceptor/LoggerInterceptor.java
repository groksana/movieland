package com.gromoks.movieland.web.interceptor;

import com.gromoks.movieland.service.security.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

    private static Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);
    private static final String GUEST = "guest";

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uuid = request.getHeader("uuid");
        String requestId = UUID.randomUUID().toString();
        String email;

        if (uuid != null) {
            email = authenticationService.getAuthenticatedUser().getEmail();
        } else {
            email = GUEST;
        }

        MDC.put("requestId", requestId);
        MDC.put("email", email);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,Object handler, Exception e) throws Exception {
        MDC.clear();
    }
}
