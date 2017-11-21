package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.UserCache;
import com.gromoks.movieland.service.UserService;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserCacheImpl implements UserCache{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private volatile Map<String,UserToken> userTokenMap = new ConcurrentHashMap<>();

    @Value("${cache.user.token.timeToLiveInMs}")
    private long timeToLiveInMs;

    @Autowired
    private UserDao userDao;

    @Override
    public UserToken getUserToken(LoginRequest loginRequest) {
        Map<String,UserToken> copyUserTokenMap = userTokenMap;
        if (copyUserTokenMap.containsKey(loginRequest.getEmail())) {
            validateLoginRequestParameter(loginRequest);
            return copyUserTokenMap.get(loginRequest.getEmail());
        } else {
            return  generateUserToken(loginRequest);
        }
    }

    @Override
    public void removeUserToken(String uuid) {
        for (String key : userTokenMap.keySet()) {
            if (userTokenMap.get(key).getUuid().equals(uuid)) {
                userTokenMap.remove(key);
                break;
            }
        }
    }

    @Override
    @PostConstruct
    @Scheduled(fixedRateString="${cache.fixedRate.user.token}")
    public void invalidate() {
        for (String key : userTokenMap.keySet()) {
            if (System.currentTimeMillis() - userTokenMap.get(key).getInitTimeInMs() >= 7200000) {
                userTokenMap.remove(key);
            }
        }
    }

    private UserToken validateLoginRequestParameter(LoginRequest loginRequest) {
        Map<String,UserToken> copyUserTokenMap = userTokenMap;
        UserToken userToken = copyUserTokenMap.get(loginRequest.getEmail());
        if (System.currentTimeMillis() - userToken.getInitTimeInMs() <= timeToLiveInMs) {
            UUID uuid = getUuid(loginRequest, userToken.getInitTimeInMs());
            if (uuid.toString().equals(userToken.getUuid())) {
                return userToken;
            } else {
                throw new IllegalArgumentException("Provided request parameters are incorrect");
            }
        } else {
            invalidate();
            return generateUserToken(loginRequest);
        }
    }

    private UserToken generateUserToken(LoginRequest loginRequest) {
        User user;
        long initTimeInMs = System.currentTimeMillis();
        try {
            user = userDao.getUserByEmail(loginRequest.getEmail());
        } catch (IllegalArgumentException e) {
            log.warn("Requested user doesn't exists. Email: {}", loginRequest.getEmail());
            throw new IllegalArgumentException(e);
        }
        checkUserCredential(loginRequest, user);
        UUID uuid = getUuid(loginRequest, initTimeInMs);
        UserToken userToken = new UserToken(String.valueOf(uuid),user.getNickname(),user.getEmail(),initTimeInMs);
        userTokenMap.put(loginRequest.getEmail(),userToken);
        return userToken;
    }

    private void checkUserCredential(LoginRequest loginRequest, User user) {
        if (!loginRequest.getPassword().equals(user.getPassword())) {
            log.warn("Password is incorrect for user {}",user.getNickname());
            throw new IllegalArgumentException("Password is incorrect for user " + user.getNickname());
        }
    }

    private UUID getUuid(LoginRequest loginRequest, long initTimeInMs) {
        return UUID.nameUUIDFromBytes((loginRequest.getEmail()+loginRequest.getPassword()+initTimeInMs).getBytes());
    }

}
