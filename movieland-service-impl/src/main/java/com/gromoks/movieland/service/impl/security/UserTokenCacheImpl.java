package com.gromoks.movieland.service.impl.security;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.service.security.UserTokenCache;
import com.gromoks.movieland.service.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.LocalDateTime.*;

@Service
public class UserTokenCacheImpl implements UserTokenCache {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<String, UserToken> uuidToUserTokenMap = new ConcurrentHashMap<>();

    @Value("${cache.user.token.timeToLiveInHours}")
    private long getTimeToLiveInHours;

    @Autowired
    private UserDao userDao;

    public UserTokenCacheImpl() {
    }

    public UserTokenCacheImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void removeCacheUserToken(String uuid) {
        log.info("Start to remove user token with uuid = {}", uuid);
        uuidToUserTokenMap.remove(uuid);
        log.info("Removed user token");
    }

    @Override
    @PostConstruct
    @Scheduled(fixedRateString = "${cache.fixedRate.user.token}", initialDelayString = "${cache.fixedRate.user.token}")
    public void invalidate() {
        log.info("Invalidate expired uuid");
        for (String key : uuidToUserTokenMap.keySet()) {
            UserToken userToken = uuidToUserTokenMap.get(key);
            if (userToken.getExpireDateTime().isBefore(now())) {
                uuidToUserTokenMap.remove(key);
                log.info("Remove expired uuid with key = {}", key);
            }
        }
    }

    @Override
    public void cacheUserToken(UserToken userToken) {
        log.info("Start to cache user token");
        uuidToUserTokenMap.put(userToken.getUuid(), userToken);
        log.info("User token has been cached for user: {}", userToken.getUser().getNickname());
    }

    @Override
    public UserToken getCacheUserTokenByUuid(String uuid) throws AuthenticationException {
        UserToken userToken = uuidToUserTokenMap.get(uuid);
        if (userToken != null) {
            return userToken;
        } else {
            log.error("Expired or invalid uuid");
            throw new AuthenticationException("Expired or invalid uuid");
        }
    }
}
