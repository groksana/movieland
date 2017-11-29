package com.gromoks.movieland.web.controller;

import com.gromoks.movieland.service.security.AuthenticationService;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.web.entity.UserTokenDto;
import com.gromoks.movieland.web.util.DtoConverter;
import com.gromoks.movieland.web.util.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody String loginRequest) throws AuthenticationException {
        log.info("Sending request to login");
        long startTime = System.currentTimeMillis();

        UserToken userToken = null;
        try {
            userToken = authenticationService.getAuthentication(loginRequest);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Requested user doesn't exists or password is incorrect");
        }
        UserTokenDto dtoUserToken = DtoConverter.toUserTokenDto(userToken);
        String json = JsonJacksonConverter.toJsonNormalUserToken(dtoUserToken);

        log.info("Login is done. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<?> logout(@RequestHeader(value = "uuid") String uuid) {
        log.info("Sending request to logout");
        long startTime = System.currentTimeMillis();

        authenticationService.logout(uuid);

        log.info("Logout is done. It tooks {} ms", System.currentTimeMillis() - startTime);
        return new ResponseEntity(HttpStatus.OK);
    }

}
