package com.gromoks.movieland.service.impl.security;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.security.UserTokenCache;
import com.gromoks.movieland.service.security.UserTokenService;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.impl.util.PasswordEncryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${cache.user.token.timeToLiveInHours}")
    private long getTimeToLiveInHours;

    @Autowired
    private UserTokenCache userTokenCache;

    @Autowired
    private UserDao userDao;

    public UserTokenServiceImpl() {}

    public UserTokenServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserToken getUserToken(LoginRequest loginRequest) throws AuthenticationException {
        return generateUserToken(loginRequest);
    }

    @Override
    public void removeUserToken(String uuid) {
        userTokenCache.removeCacheUserToken(uuid);
    }

    @Override
    public UserToken getUserTokenByUuid(String uuid) throws AuthenticationException {
        log.info("Start to get user token by uuid");
        UserToken userToken = userTokenCache.getCacheUserTokenByUuid(uuid);

        if (userToken.getExpireDateTime().isAfter(now())) {
            return userToken;
        } else {
            User user = userToken.getUser();
            String regeneratedUuid = getUuid(user.getEmail(), userToken.getExpireDateTime());
            LocalDateTime expireDataTime = now().plusHours(getTimeToLiveInHours);
            User regeneratedUser = new User(user.getId(),user.getNickname(),user.getEmail(),user.getRole());
            UserToken regeneratedUserToken = new UserToken(regeneratedUuid,regeneratedUser,expireDataTime);

            userTokenCache.removeCacheUserToken(uuid);
            userTokenCache.cacheUserToken(regeneratedUserToken);

            log.info("User token by uuid has been got");
            return regeneratedUserToken;
        }
    }

    private UserToken generateUserToken(LoginRequest loginRequest) throws AuthenticationException {
        log.info("Start to generate user token uuid");

        LocalDateTime expireDataTime = now().plusHours(getTimeToLiveInHours);
        String password = PasswordEncryption.encryptPassword(loginRequest.getPassword());
        User user = userDao.getUserByEmailAndPassword(loginRequest.getEmail(), password);
        String uuid = getUuid(loginRequest.getEmail(), expireDataTime);
        UserToken userToken = new UserToken(uuid,user,expireDataTime);
        userTokenCache.cacheUserToken(userToken);

        log.info("User token has been generated for user {}",user.getNickname());
        return userToken;
    }

    private String getUuid(String email, LocalDateTime expireDateTime) {
        return UUID.nameUUIDFromBytes((email+expireDateTime.toString()).getBytes()).toString();
    }

}
