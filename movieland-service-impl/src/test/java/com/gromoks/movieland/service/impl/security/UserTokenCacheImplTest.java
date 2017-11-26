package com.gromoks.movieland.service.impl.security;

import com.gromoks.movieland.dao.jdbc.UserDao;
import com.gromoks.movieland.entity.User;
import com.gromoks.movieland.service.security.UserTokenCache;
import com.gromoks.movieland.service.security.UserTokenService;
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

import javax.naming.AuthenticationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class UserTokenCacheImplTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Mock
    UserDao mockUserDao;

    @Mock
    UserTokenCache mockUserTokenCache;

    @InjectMocks
    UserTokenService userTokenService = new UserTokenServiceImpl(mockUserDao);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        User user = new User();
        user.setId(1);
        user.setNickname("Testnick");
        user.setEmail("test@email.com");
        String password = null;
        password = PasswordEncryption.encryptPassword("testpassword");
        try {
            when(mockUserDao.getUserByEmailAndPassword("test@email.com",password)).thenReturn(user);
        } catch (AuthenticationException e) {
            log.warn("Requested user doesn't exists or password is incorrect. Email: {}", "test@email.com");
        }
    }

    @Test
    public void testGetUserToken() throws AuthenticationException {
        LoginRequest loginRequest = new LoginRequest("test@email.com","testpassword");
        UserToken userToken = userTokenService.getUserToken(loginRequest);
        assertEquals(userToken.getUser().getNickname(),"Testnick");
        assertNotNull(userToken.getUuid());
    }
}
