package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.UserCache;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.impl.util.PasswordEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserCacheImpl implements UserCache{

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<String,UserToken> userTokenMap = new ConcurrentHashMap<>();

    @Value("${cache.user.token.timeToLiveInMs}")
    private long timeToLiveInMs;

    @Autowired
    private UserDao userDao;

    public UserCacheImpl() {

    }

    public UserCacheImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserToken getUserToken(LoginRequest loginRequest) {
        if (userTokenMap.containsKey(loginRequest.getEmail())) {
            validateLoginRequestParameter(loginRequest);
            return userTokenMap.get(loginRequest.getEmail());
        } else {
            return  generateUserToken(loginRequest);
        }
    }

    @Override
    public void removeUserToken(String uuid) {
        log.info("Start to remove user token with uuid = {}",uuid);
        for (String key : userTokenMap.keySet()) {
            if (userTokenMap.get(key).getUuid().equals(uuid)) {
                userTokenMap.remove(key);
                log.info("Removed user token");
                break;
            }
        }
    }

    @Override
    @PostConstruct
    @Scheduled(fixedRateString="${cache.fixedRate.user.token}")
    public void invalidate() {
        log.info("Invalidate expired uuid");
        for (String key : userTokenMap.keySet()) {
            if (System.currentTimeMillis() >= userTokenMap.get(key).getExpireTimeInMs()) {
                userTokenMap.remove(key);
                log.info("Remove expired uuid with key = {}",key);
            }
        }
    }

    private UserToken validateLoginRequestParameter(LoginRequest loginRequest) {
        UserToken userToken = userTokenMap.get(loginRequest.getEmail());
        if (System.currentTimeMillis() < userToken.getExpireTimeInMs()) {
            String uuid = getUuid(loginRequest, userToken.getExpireTimeInMs());
            if (uuid.equals(userToken.getUuid())) {
                return userToken;
            } else {
                throw new IllegalArgumentException("Provided request parameters are incorrect");
            }
        } else {
            invalidate();
            return generateUserToken(loginRequest);
        }
    }

    UserToken generateUserToken(LoginRequest loginRequest) {
        log.info("Start to generate user token uuid");
        User user = null;
        long expireTimeInMs = System.currentTimeMillis() + timeToLiveInMs;
        try {
            String password = PasswordEncryption.encryptPassword(loginRequest.getPassword());
            user = userDao.getUserByEmailAndPassword(loginRequest.getEmail(), password);
        } catch (IllegalArgumentException e) {
            log.warn("Requested user doesn't exists or password is incorrect. Email: {}", loginRequest.getEmail());
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e) {
            log.error("Sorry, but MD5 is not a valid message digest algorithm");
        }
        String uuid = getUuid(loginRequest, expireTimeInMs);
        UserToken userToken = new UserToken(uuid,user.getNickname(),user.getEmail(),expireTimeInMs);
        userTokenMap.put(loginRequest.getEmail(),userToken);
        log.info("User token has been generated for user {}",user.getNickname());
        return userToken;
    }

    private String getUuid(LoginRequest loginRequest, long expireTimeInMs) {
        return UUID.nameUUIDFromBytes((loginRequest.getEmail()+loginRequest.getPassword()+expireTimeInMs).getBytes()).toString();
    }

}
