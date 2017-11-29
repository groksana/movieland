package com.gromoks.movieland.service.impl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gromoks.movieland.service.entity.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUserRequestConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger log = LoggerFactory.getLogger(JsonUserRequestConverter.class);

    public static LoginRequest convertJsonToUserRequest(String jsonLoginRequest) {
        try {
            return objectMapper.readValue(jsonLoginRequest, LoginRequest.class);
        } catch (IOException e) {
            log.warn("Issue to get json for login request");
            throw new RuntimeException("Issue to get json for login request");
        }
    }

}
