package com.gromoks.movieland.service.impl;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.UserCache;
import com.gromoks.movieland.service.entity.LoginRequest;
import com.gromoks.movieland.service.entity.UserToken;
import com.gromoks.movieland.service.impl.util.PasswordEncryption;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserCacheImplTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Mock
    UserDao mockUserDao;

    @InjectMocks
    UserCache userCache = new UserCacheImpl(mockUserDao);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setId(1);
        user.setNickname("Testnick");
        user.setEmail("test@email.com");
        String password = null;
        try {
            password = PasswordEncryption.encryptPassword("testpassword");
        } catch (NoSuchAlgorithmException e) {
            log.error("Sorry, but MD5 is not a valid message digest algorithm");
        }
        when(mockUserDao.getUserByEmailAndPassword("test@email.com",password)).thenReturn(user);
    }

    @Test
    public void testGetUserToken() {
        LoginRequest loginRequest = new LoginRequest("test@email.com","testpassword");
        UserToken userToken = userCache.getUserToken(loginRequest);
        assertEquals(userToken.getNickname(),"Testnick");
        assertNotNull(userToken.getUuid());
    }
}
